package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.InMemoryFilmService;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService filmService = new InMemoryUserService(userStorage);
    private final UserController controller = new UserController(filmService);


    @Test
    void getLsUsers() {
        controller.postUser(new User(3, "Волк@mail.ru", "логин",
                "Валера", LocalDate.now()));
        assertNotNull(controller.getLsUsers());
    }

    @Test
    void postUser() {
        assertNotNull(controller.postUser(new User(3, "Волк@mail.ru", "логин",
                "Валера", LocalDate.now())));
    }

    @Test
    void postUserNullEmail() {
        try {
            assertNotNull(controller.postUser(new User(3, null, "логин",
                    "Валера", LocalDate.now())));
        } catch (ValidateException ex) {
            assertEquals("Некорректный User", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void postUserInvalidEmail() {
        try {
            assertNotNull(controller.postUser(new User(3, "Волкmail.ru", "логин",
                    "Валера", LocalDate.now())));
        } catch (ValidateException ex) {
            assertEquals("Некорректный User", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void postUserNullInvalidLogin() {
        try {
            assertNotNull(controller.postUser(new User(3, "Волк@mail.ru", null,
                    "Валера", LocalDate.now())));
        } catch (ValidateException ex) {
            assertEquals("Некорректный User", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void postUserNullInvalidLoginSpace() {
        try {
            assertNotNull(controller.postUser(new User(3, "Волк@mail.ru", "логин dfdf",
                    "Валера", LocalDate.now())));
        } catch (ValidateException ex) {
            assertEquals("Некорректный User", ex.getMessage());
            return;
        }
        fail();
    }


    @Test
    void updateUser() {
        controller.postUser(new User(1, "Волк@mail.ru", "логин",
                "Валера", LocalDate.now()));
        User user = new User(1, "Волк2@mail.ru", "2",
                "Витек", LocalDate.now());
        controller.updateUser(user);
        assertEquals(user, controller.getLsUsers().get(0));
    }

    @Test
    void addFriend() {
        User user = new User(1, "fddf@mail.ru", "dsdd", "ssds", LocalDate.now());
        User user2 = new User(2, "fddf@mail.ru", "dsdd", "Вася", LocalDate.now());

        controller.postUser(user);
        controller.postUser(user2);
        controller.addFriend(1, 2);
        controller.getUser(1);
    }


}