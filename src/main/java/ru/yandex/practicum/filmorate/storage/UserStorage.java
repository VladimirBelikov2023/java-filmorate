package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> getLsUsers();

    User postUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    Map<Integer, User> getMap();

    int getId();

}
