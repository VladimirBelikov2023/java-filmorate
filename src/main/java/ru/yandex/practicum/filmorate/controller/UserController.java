package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MyException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;

    private final Map<Integer, User> lsUser = new HashMap<>();

    @GetMapping
    public List<User> getLsUsers() {
        return new ArrayList<>(lsUser.values());
    }

    @PostMapping
    public User postUser(@RequestBody User us) {
        log.debug("Пост User");
        User user = new User(id, us.getEmail(), us.getLogin(), us.getName(), us.getBirthday());
        if (us.getName() == null) {
            User user1 = new User(id, us.getEmail(), us.getLogin(), us.getLogin(), us.getBirthday());
            if (!isValid(user1)) {
                log.warn("Некорректный User");
                throw new MyException("Некорректный User");
            } else {
                lsUser.put(id, user1);
                id += 1;
                return user1;
            }
        } else {
            if (!isValid(us)) {
                log.warn("Некорректный User");
                throw new MyException("Некорректный User");
            } else {
                lsUser.put(id, user);
                id += 1;
                return user;
            }
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Обновление User");
        if (!lsUser.containsKey(user.getId())) {
            log.warn("User существует");
            throw new MyException("Такого Userа нет");
        }
        if (user.getName() == null) {
            User user1 = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
            if (!isValid(user1)) {
                log.warn("Некорректный User");
                throw new MyException("Некорректный User");
            } else {
                lsUser.put(user.getId(), user1);
                return user1;
            }
        } else {
            if (!isValid(user)) {
                log.warn("Некорректный User");
                throw new MyException("Некорректный User");
            } else {
                lsUser.put(user.getId(), user);
                return user;
            }
        }
    }

    private boolean isValid(User user) {
        return user.getEmail() != null && user.getLogin() != null && user.getBirthday() != null && !user.getEmail().isEmpty() && user.getEmail().contains("@") &&
                !user.getLogin().isEmpty() && !user.getLogin().contains(" ") && !user.getBirthday().isAfter(LocalDate.now());
    }
}
