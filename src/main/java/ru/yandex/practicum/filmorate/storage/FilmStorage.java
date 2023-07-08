package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getLsFilms();

    Film postFilm(Film film);

    Film update(Film film);

    void deleteFilm(int id);

    Film getFilm(int id);

    public List<Film> getPopular(int id);

    void addLike(int idUser, int idFilm);

    void delLike(int idUser, int idFilm);

}
