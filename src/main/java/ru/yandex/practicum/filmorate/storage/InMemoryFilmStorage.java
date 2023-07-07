package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;


public class InMemoryFilmStorage implements FilmStorage {


    private final Map<Integer, Film> lsFilm = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> getLsFilms() {
        return new ArrayList<>(lsFilm.values());
    }

    @Override
    public Film getFilm(int id) {
        return lsFilm.get(id);
    }


    @Override
    public Film postFilm(@RequestBody @Valid Film film) {
        lsFilm.put(film.getId(), film);
        id += 1;
        return film;

    }


    @Override
    public Film update(@RequestBody @Valid Film film) {
        lsFilm.put(film.getId(), film);
        return film;

    }

    @Override
    public void deleteFilm(int id) {
        lsFilm.remove(id);
    }

    public Map<Integer, Film> getMap() {
        return lsFilm;
    }

    public int getId() {
        return id;
    }

    public List<Film> getPopular(int id) {
        return null;
    }

    public void addLike(int idUser, int idFilm) {

    }

    public void delLike(int idUser, int idFilm) {

    }


}
