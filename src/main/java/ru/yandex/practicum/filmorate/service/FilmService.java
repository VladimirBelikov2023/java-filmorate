package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getLsFilms();

    Film postFilm(Film film);

    Film update(Film film);

    void deleteFilm(int id);

    void addLike(int idUser, int idFilm);

    void delLike(int idUser, int idFilm);

    List<Film> getTop(int max);

    Film getFilm(int id);
}
