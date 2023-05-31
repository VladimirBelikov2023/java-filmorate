package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.MyException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private final FilmController controller = new FilmController();

    @Test
    void getLsFilms() {
        controller.postFilm(new Film(3, "Волк с Уолл-Стрит", "Мой любимый фильм",
                LocalDate.of(2000, 3, 1), 10));
        assertNotNull(controller.getLsFilms());
    }

    @Test
    void postFilm() {
        assertNotNull(controller.postFilm(new Film(3, "Волк с Уолл-Стрит",
                "Мой любимый фильм", LocalDate.of(2000, 3, 1), 10)));
    }

    @Test
    void postFilmNullName() {
        try {
            assertNotNull(controller.postFilm(new Film(3, null,
                    "Мой любимый фильм", LocalDate.of(2000, 3, 1), 10)));
        } catch (MyException ex) {
            assertEquals("Некорректный фильм", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void postFilmNullInvalidDescription() {
        try {
            assertNotNull(controller.postFilm(new Film(3, "Волк с Уолл-Стрит",
                    "Мой любимый фильм".repeat(200), LocalDate.of(2000, 3, 1), 10)));
        } catch (MyException ex) {
            assertEquals("Некорректный фильм", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void postFilmNullInvalidDuration() {
        try {
            assertNotNull(controller.postFilm(new Film(3, "Волк с Уолл-Стрит",
                    "Мой любимый фильм", LocalDate.of(2000, 3, 1), -10)));
        } catch (MyException ex) {
            assertEquals("Некорректный фильм", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void postFilmNullInvalidData() {
        try {
            assertNotNull(controller.postFilm(new Film(3, "Волк с Уолл-Стрит",
                    "Мой любимый фильм", LocalDate.of(1000, 3, 1), 10)));
        } catch (MyException ex) {
            assertEquals("Некорректный фильм", ex.getMessage());
            return;
        }
        fail();
    }

    @Test
    void updateFilm() {
        controller.postFilm(new Film(1, "Волк с Уолл-Стрит", "Мой любимый фильм",
                LocalDate.of(2000, 3, 1), 10));
        Film film = new Film(1, "Волк с Уолл-Стрит2", "Мой нелюбимый фильм",
                LocalDate.of(2010, 3, 1), 10);
        controller.update(film);
        assertEquals(film, controller.getLsFilms().get(0));
    }

    @Test
    void updateFilmNotExist() {
        try {
            Film film = new Film(3, "Волк с Уолл-Стрит2", "Мой нелюбимый фильм",
                    LocalDate.of(2010, 3, 1), 10);
            controller.update(film);
        } catch (MyException ex) {
            assertEquals("Такого фильма нет", ex.getMessage());
            return;
        }
        fail();
    }


    @Test
    void updateFilmFilmNullName() {
        Film film = new Film(1, "Волк с Уолл-Стрит2", "Мой нелюбимый фильм",
                LocalDate.of(2010, 3, 1), 10);
        controller.postFilm(film);
        try {
            controller.update(new Film(1, null,
                    "Мой любимый фильм", LocalDate.of(2000, 3, 1), 10));
        } catch (MyException ex) {
            assertEquals("Некорректный фильм", ex.getMessage());
            return;
        }
        fail();
    }
}