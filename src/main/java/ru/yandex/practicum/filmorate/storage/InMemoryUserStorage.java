package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> lsUser = new HashMap<>();

    @Override
    public List<User> getLsUsers() {
        return new ArrayList<>(lsUser.values());
    }


    @Override
    public User postUser(User us) {
        lsUser.put(id, us);
        id += 1;
        return us;
    }

    @Override
    public User updateUser(User user) {
        lsUser.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        lsUser.remove(id);
    }

    public Map<Integer, User> getMap() {
        return lsUser;
    }

    public int getId() {
        return id;
    }
}
