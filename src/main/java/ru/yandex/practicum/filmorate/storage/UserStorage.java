package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getLsUsers();

    User getUser(int id);

    User postUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    void addFriend(int us1, int us2);

    void delFriend(int us1, int us2);


}
