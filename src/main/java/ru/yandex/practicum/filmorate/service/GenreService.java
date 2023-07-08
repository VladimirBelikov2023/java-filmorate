package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getLsGenre();

    Genre getGenre(int id);
}
