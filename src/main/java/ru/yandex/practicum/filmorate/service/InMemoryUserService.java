package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnknownException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getLsUsers() {
        return new ArrayList<>(userStorage.getLsUsers());
    }

    @Override
    public User getUser(int id) {
        log.debug("получаем по id User");
        try {
            return userStorage.getUser(id);
        } catch (Exception e) {
            throw new UnknownException("Wrong id of User");
        }
    }

    @Override
    public List<User> getFriend(int id) {
        log.debug("получаем друзей User");
        User user = userStorage.getUser(id);
        ArrayList<User> ans = new ArrayList<>();
        Set<User> lss = user.getLsFriends();
        for (User o : lss) {
            ans.add(o);
        }
        return ans;
    }


    @Override
    public User postUser(User us) {
        log.debug("Пост User");
        if (us.getName() == null) {
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
        HashMap<Integer, User> ll = getUsersCh();
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!ll.containsKey(user.getId())) {
            log.warn("Некорректный User");
            throw new UnknownException("Некорректный User");
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
        HashMap<Integer, User> ll = getUsersCh();
        if (!ll.containsKey(idUser1)) {
            log.warn("Пользователь не найден");
            throw new UnknownException("Пользователь не найден");
        } else if (!ll.containsKey(idUser2)) {
            log.warn("Друг не найден");
            throw new UnknownException("Друг не найден");
        } else {
            userStorage.addFriend(idUser1, idUser2);
        }
    }

    @Override
    public List<User> getLsFriends(int idUser1, int idUser2) {
        log.debug("добавление друзей");
        HashMap<Integer, User> ll = getUsersCh();
        if (!ll.containsKey(idUser1)) {
            log.warn("Первый друг не найден");
            throw new UnknownException("User не найден");
        } else if (!ll.containsKey(idUser2)) {
            log.warn("Второй друг не найден");
            throw new UnknownException("Друг не найден");
        } else {
            User user1 = ll.get(idUser1);
            User user2 = ll.get(idUser2);
            List<User> comFriends = new ArrayList<>();
            for (User o : user1.getLsFriends()) {
                for (User b : user2.getLsFriends()) {
                    if (Objects.equals(o, b)) {
                        comFriends.add(ll.get(o.getId()));
                    }
                }
            }
            return comFriends;
        }
    }

    @Override
    public void deleteFriend(int idUser1, int idUser2) {
        log.debug("удаление друзей");
        HashMap<Integer, User> ll = getUsersCh();
        if (!ll.containsKey(idUser1)) {
            log.warn("Друг не найден");
            throw new UnknownException("Пользователь не найден");
        } else if (!ll.containsKey(idUser2)) {
            log.warn("Друг не найден");
            throw new UnknownException("Друг не найден");
        } else {
            userStorage.delFriend(idUser1, idUser2);
        }

    }

    private HashMap<Integer, User> getUsersCh() {
        List<User> lss = getLsUsers();
        HashMap<Integer, User> ans = new HashMap<>();
        for (User o : lss) {
            ans.put(o.getId(), o);
        }
        return ans;
    }

    private boolean isValid(User user) {
        return user.getEmail() != null && user.getLogin() != null && user.getBirthday() != null && !user.getEmail().isEmpty() && user.getEmail().contains("@") &&
                !user.getLogin().trim().isEmpty() && !user.getLogin().contains(" ") && !user.getBirthday().isAfter(LocalDate.now());
    }
}
