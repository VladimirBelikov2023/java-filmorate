package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getLsUsers() {
        List<User> ls = new ArrayList<>(userService.getLsUsers());
        System.out.println(ls);
        return ls;
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


}
