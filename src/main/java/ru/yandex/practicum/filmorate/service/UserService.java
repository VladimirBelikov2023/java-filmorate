package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getLsUsers();

    User getUser(int id);

    User postUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    void addFriend(int idUser1, int idUser2);

    List<User> getLsFriends(int idUser1, int idUser2);

    void deleteFriend(int idUser1, int idUser2);

    List<User> getFriend(int id);
}
