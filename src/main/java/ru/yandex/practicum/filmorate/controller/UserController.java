package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
    public User postUser(@RequestBody @Valid User us) {
        log.debug("Пост User");
        us.setId(id);
        if (us.getName() == null) {
            us.setName(us.getLogin());
        }
        if (!isValid(us)) {
            log.warn("Некорректный User");
            throw new ValidateException("Некорректный User");
        } else {
            lsUser.put(id, us);
            id += 1;
            return us;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.debug("Обновление User");
        if (!lsUser.containsKey(user.getId())) {
            log.warn("User существует");
            throw new ValidateException("Такого Userа нет");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!isValid(user)) {
            log.warn("Некорректный User");
            throw new ValidateException("Некорректный User");
        } else {
            lsUser.put(user.getId(), user);
            return user;
        }
    }

    private boolean isValid(User user) {
        return user.getEmail() != null && user.getLogin() != null && user.getBirthday() != null && !user.getEmail().isEmpty() && user.getEmail().contains("@") &&
                !user.getLogin().isEmpty() && !user.getLogin().contains(" ") && !user.getBirthday().isAfter(LocalDate.now());
    }
}
