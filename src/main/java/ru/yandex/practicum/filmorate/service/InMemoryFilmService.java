package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnknownException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service

public class InMemoryFilmService implements FilmService {
    private final LocalDate dataVal = LocalDate.of(1895, 11, 28);
    private final FilmStorage filmStorage;


    @Override
    public List<Film> getLsFilms() {
        log.debug("Получение фильмов");
        try {
            return new ArrayList<>(filmStorage.getLsFilms());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Film getFilm(int id) {
        log.debug("Получение фильма по id фильма");
        HashMap<Integer, Film> ll = getFilmCh();
        if (!ll.containsKey(id)) {
            throw new UnknownException("Wrong id");
        }
        try {
            return filmStorage.getFilm(id);
        } catch (Exception e) {
            log.warn("Фильм не существует");
            throw new ValidateException(e.getMessage());
        }
    }

    @Override
    public Film postFilm(Film film) {
        log.debug("Пост фильма");
        if (!isValid(film)) {
            throw new ValidateException("Wrong film");
        }
        try {
            return filmStorage.postFilm(film);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ValidateException(e.getMessage());
        }
    }

    @Override
    public Film update(Film film) {
        log.debug("Обновление фильма");
        HashMap<Integer, Film> ll = getFilmCh();
        if (!isValid(film)) {
            throw new ValidateException("Wrong film");
        }
        if (!ll.containsKey(film.getId())) {
            throw new UnknownException("Wrong id");
        }
        try {
            return filmStorage.update(film);
        } catch (Exception e) {
            log.warn("Некорректный фильм");
            throw new ValidateException(e.getMessage());
        }
    }

    @Override
    public void deleteFilm(int id) {
        HashMap<Integer, Film> ll = getFilmCh();
        if (!ll.containsKey(id)) {
            throw new UnknownException("Wrong id");
        }
        try {
            filmStorage.deleteFilm(id);
        } catch (Exception e) {
            log.warn("Некорректный id фильм");
            throw new ValidateException(e.getMessage());
        }
    }


    @Override
    public void addLike(int idUser, int idFilm) {
        HashMap<Integer, Film> ll = getFilmCh();
        if (!ll.containsKey(idUser)) {
            throw new UnknownException("Wrong id User");
        }
        if (!ll.containsKey(idFilm)) {
            throw new UnknownException("Wrong id Film");
        }
        try {
            filmStorage.addLike(idUser, idFilm);
        } catch (Exception e) {
            throw new ValidateException("Wrong id of film like");
        }
    }

    @Override
    public void delLike(int idUser, int idFilm) {
        HashMap<Integer, Film> ll = getFilmCh();
        if (!ll.containsKey(idUser)) {
            throw new UnknownException("Wrong id User");
        }
        if (!ll.containsKey(idFilm)) {
            throw new UnknownException("Wrong id Film");
        }
        try {
            filmStorage.delLike(idUser, idFilm);
        } catch (Exception e) {
            throw new ValidateException("Wrong id of film like");
        }
    }

    @Override
    public List<Film> getPopular(int max) {
        return filmStorage.getPopular(max);
    }


    private boolean isValid(Film film) {
        return film.getName() != null && film.getReleaseDate() != null && !film.getName().isEmpty() && film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(dataVal) &&
                film.getDuration() >= 0;
    }

    private HashMap<Integer, Film> getFilmCh() {
        List<Film> lss = getLsFilms();
        HashMap<Integer, Film> ans = new HashMap<>();
        for (Film o : lss) {
            ans.put(o.getId(), o);
        }
        return ans;
    }
}
