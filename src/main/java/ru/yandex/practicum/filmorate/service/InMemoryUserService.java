package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnknownException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getLsUsers() {
        return new ArrayList<>(userStorage.getMap().values());
    }

    @Override
    public User getUser(int id) {
        log.debug("получаем по id User");
        if (!userStorage.getMap().containsKey(id)) {
            log.warn("Некорректный id");
            throw new UnknownException("Некорректный id");
        }
        return userStorage.getMap().get(id);
    }

    @Override
    public List<User> getFriend(int id) {
        log.debug("получаем друзей User");
        if (!userStorage.getMap().containsKey(id)) {
            log.warn("Некорректный id");
            throw new UnknownException("Некорректный id");
        }
        User user = userStorage.getMap().get(id);
        List<User> ls = new ArrayList<>();
        for (Integer o : user.getLsFriends()) {
            ls.add(userStorage.getMap().get(o));
        }
        return ls;
    }


    @Override
    public User postUser(User us) {
        log.debug("Пост User");
        us.setId(userStorage.getId());
        if (us.getName() == null || us.getName().isEmpty()) {
            us.setName(us.getLogin());
        }
        if (!isValid(us)) {
            log.warn("Некорректный User");
            throw new ValidateException("Некорректный User");
        } else {
            return userStorage.postUser(us);
        }
    }

    @Override
    public User updateUser(User user) {
        log.debug("Обновление User");
        if (!userStorage.getMap().containsKey(user.getId())) {
            log.warn("User не существует");
            throw new UnknownException("Такого Userа нет");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!isValid(user)) {
            log.warn("Некорректный User");
            throw new ValidateException("Некорректный User");
        } else {
            return userStorage.updateUser(user);
        }
    }


    @Override
    public void addFriend(int idUser1, int idUser2) {
        log.warn("добавление друзей");
        if (!userStorage.getMap().containsKey(idUser1)) {
            log.warn("Друг не найден");
            throw new UnknownException("Друг не найден");
        } else if (!userStorage.getMap().containsKey(idUser2)) {
            log.warn("Друг не найден");
            throw new UnknownException("Друг не найден");
        } else if (userStorage.getMap().get(idUser1).getLsFriends().contains(userStorage.getMap().get(idUser2))) {
            log.warn("Они и так друзья");
            throw new ValidateException("Они и так друзья");
        } else {
            userStorage.getMap().get(idUser1).addFriend(idUser2);
            userStorage.getMap().get(idUser2).addFriend(idUser1);
        }
    }

    @Override
    public List<User> getLsFriends(int idUser1, int idUser2) {
        log.debug("добавление друзей");
        if (!userStorage.getMap().containsKey(idUser1)) {
            log.warn("Первый друг не найден");
            throw new UnknownException("Друг не найден");
        } else if (!userStorage.getMap().containsKey(idUser2)) {
            log.warn("Второй друг не найден");
            throw new UnknownException("Друг не найден");
        } else {
            User user1 = userStorage.getMap().get(idUser1);
            User user2 = userStorage.getMap().get(idUser2);
            List<User> comFriends = new ArrayList<>();
            for (Integer o : user1.getLsFriends()) {
                for (Integer b : user2.getLsFriends()) {
                    if (Objects.equals(o, b)) {
                        comFriends.add(userStorage.getMap().get(o));
                    }
                }
            }
            return comFriends;
        }
    }

    @Override
    public void deleteFriend(int idUser1, int idUser2) {
        log.debug("удаление друзей");
        if (!userStorage.getMap().containsKey(idUser1)) {
            log.warn("Друг не найден");
            throw new UnknownException("Пользователь не найден");
        } else if (!userStorage.getMap().containsKey(idUser2)) {
            log.warn("Друг не найден");
            throw new UnknownException("Друг не найден");
        } else if (!userStorage.getMap().get(idUser1).getLsFriends().contains(userStorage.getMap().get(idUser2).getId())) {
            log.warn("Они не друзья");
            throw new ValidateException("Они не друзья");
        } else {
            User user1 = userStorage.getMap().get(idUser1);
            User user2 = userStorage.getMap().get(idUser2);
            user1.deleteFriend(user2);
            user2.deleteFriend(user1);
        }

    }


    @Override
    public void deleteUser(int id) {
        log.debug("удаление Usera");
        if (!userStorage.getMap().containsKey(id)) {
            log.debug(" Такого пользователя нет");
            throw new UnknownException("Такого пользователя нет");
        }
        userStorage.deleteUser(id);
    }

    private boolean isValid(User user) {
        return user.getEmail() != null && user.getLogin() != null && user.getBirthday() != null && !user.getEmail().isEmpty() && user.getEmail().contains("@") &&
                !user.getLogin().trim().isEmpty() && !user.getLogin().contains(" ") && !user.getBirthday().isAfter(LocalDate.now());
    }
}
