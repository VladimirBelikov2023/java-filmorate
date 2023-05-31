package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final static LocalDate validDate = LocalDate.of(1895, 11, 28);
    private final Map<Integer, Film> lsFilm = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> getLsFilms() {
        return new ArrayList<>(lsFilm.values());
    }

    @PostMapping
    public Film postFilm(@RequestBody @Valid Film film) {
        log.debug("Пост фильма");
        film.setId(id);


        if (lsFilm.containsKey(film.getId())) {
            log.warn("Фильм существует");
            throw new ValidateException("Фильм существует");
        } else if (!isValid(film)) {
            log.warn("Некорректный фильм");
            throw new ValidateException("Некорректный фильм");
        } else {
            lsFilm.put(film.getId(), film);
            id += 1;
            return film;
        }
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Обновление фильма");

        if (!lsFilm.containsKey(film.getId())) {
            log.warn("Такого фильма нет");
            throw new ValidateException("Такого фильма нет");
        } else if (!isValid(film)) {
            log.warn("Некорректный фильм");
            throw new ValidateException("Некорректный фильм");
        } else {
            lsFilm.put(film.getId(), film);
            return film;
        }
    }

    private boolean isValid(Film film) {
        return film.getName() != null && film.getReleaseDate() != null && !film.getName().isEmpty() && film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(validDate) &&
                film.getDuration() >= 0;
    }


}
