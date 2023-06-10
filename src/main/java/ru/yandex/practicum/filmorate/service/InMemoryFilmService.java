package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnknownException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service

public class InMemoryFilmService implements FilmService {
    private final LocalDate dataVal = LocalDate.of(1895, 11, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Override
    public List<Film> getLsFilms() {
        return new ArrayList<>(filmStorage.getLsFilms());
    }

    @Override
    public Film getFilm(int id) {
        log.debug("Получение фильма по id фильма");
        if (!filmStorage.getMap().containsKey(id)) {
            log.warn("Фильм не существует");
            throw new UnknownException("Фильм не существует");
        }
        return filmStorage.getFilm(id);
    }

    @Override
    public Film postFilm(Film film) {
        log.debug("Пост фильма");
        film.setId(filmStorage.getId());

        if (filmStorage.getMap().containsKey(film.getId())) {
            log.warn("Фильм существует");
            throw new ValidateException("Фильм существует");
        } else if (!isValid(film)) {
            log.warn("Некорректный фильм");
            throw new ValidateException("Некорректный фильм");
        } else {
            return filmStorage.postFilm(film);
        }
    }

    @Override

    public Film update(Film film) {
        log.debug("Обновление фильма");

        if (!filmStorage.getMap().containsKey(film.getId())) {
            log.warn("Такого фильма нет");
            throw new UnknownException("Такого фильма нет");
        } else if (!isValid(film)) {
            log.warn("Некорректный фильм");
            throw new ValidateException("Некорректный фильм");
        } else {
            return filmStorage.update(film);
        }
    }

    @Override
    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }


    @Override
    public void addLike(int idUser, int idFilm) {
        log.debug("Ставим лайк");
        if (!userStorage.getMap().containsKey(idUser)) {
            log.warn("Такого Пользователя нет");
            throw new UnknownException("Такого Пользователя нет");
        } else if (!filmStorage.getMap().containsKey(idFilm)) {
            log.warn("Такого фильма нет");
            throw new UnknownException("Такого фильма нет");
        } else if (filmStorage.getMap().get(idFilm).getLsLikes().contains(userStorage.getMap().get(idUser))) {
            log.warn("Лайк уже поставлен");
            throw new ValidateException("Лайк уже поставлен");
        } else {
            User user = userStorage.getMap().get(idUser);
            filmStorage.getMap().get(idFilm).addLike(user);
        }
    }

    @Override
    public void delLike(int idUser, int idFilm) {
        log.debug("Удаляем лайк");
        if (!userStorage.getMap().containsKey(idUser)) {
            log.warn("Такого Пользователя нет");
            throw new UnknownException("Такого Пользователя нет");
        } else if (!filmStorage.getMap().containsKey(idFilm)) {
            log.warn("Такого фильма нет");
            throw new UnknownException("Такого фильма нет");
        } else if (!filmStorage.getMap().get(idFilm).getLsLikes().contains(userStorage.getMap().get(idUser))) {
            log.warn("Лайка и так нет");
            throw new ValidateException("Лайка и так нет");
        } else {
            User user = userStorage.getLsUsers().get(idUser);
            filmStorage.getMap().get(idFilm).delLike(user);
        }
    }

    @Override
    public List<Film> getTop(int max) {
        log.debug("Получаем топ");
        Comparator<Film> filmComparator = (film1, film2) -> film1.getLsLikes().size() - film2.getLikes().size();
        Comparator<Film> f2 = filmComparator.reversed();
        List<Film> ls = filmStorage.getLsFilms();
        ls.sort(f2);
        return ls.stream().limit(max).collect(Collectors.toList());
    }


    private boolean isValid(Film film) {
        return film.getName() != null && film.getReleaseDate() != null && !film.getName().isEmpty() && film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(dataVal) &&
                film.getDuration() >= 0;
    }
}
