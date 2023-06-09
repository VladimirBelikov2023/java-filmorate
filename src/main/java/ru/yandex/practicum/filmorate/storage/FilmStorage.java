package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    List<Film> getLsFilms();

    Film postFilm(Film film);

    Film update(Film film);

    void deleteFilm(int id);

    Map<Integer, Film> getMap();

    int getId();

    Film getFilm(int id);

}
