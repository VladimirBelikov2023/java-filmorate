package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    @BeforeEach
    public void start() {
        controller = new UserController();
    }

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
    void updateUserNotExist() {
        try {
            User user = (new User(3, "Волк@mail.ru", "логин",
                    "Валера", LocalDate.now()));
            controller.updateUser(user);
        } catch (ValidateException ex) {
            assertEquals("Такого Userа нет", ex.getMessage());
            return;
        }
        fail();
    }


}