package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getLsUsers() {
        return new ArrayList<>(userService.getLsUsers());
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{idUser1}/friends/{idUser2}")
    public void addFriend(@PathVariable int idUser1, @PathVariable int idUser2) {
        userService.addFriend(idUser1, idUser2);
    }

    @DeleteMapping("/users/{idUser1}/friends/{idUser2}")
    public void delFriend(@PathVariable int idUser1, @PathVariable int idUser2) {
        userService.deleteFriend(idUser1, idUser2);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriend(@PathVariable int id) {
        return userService.getFriend(id);
    }

    @GetMapping("/users/{idUser1}/friends/common/{idUser2}")
    public List<User> getLsFriends(@PathVariable int idUser1, @PathVariable int idUser2) {
        return userService.getLsFriends(idUser1, idUser2);
    }


    @PostMapping("/users")
    public User postUser(@RequestBody @Valid User us) {
        return userService.postUser(us);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/users")
    public void deleteUser(int id) {
        userService.deleteUser(id);
    }


}
