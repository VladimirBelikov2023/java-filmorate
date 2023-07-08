package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnknownException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class InMemoryGenreService implements GenreService {
    GenreStorage genreStorage;

    @Override
    public List<Genre> getLsGenre() {
        return genreStorage.getLsGenre();
    }

    @Override
    public Genre getGenre(int id) {
        try {
            return genreStorage.getGenre(id);
        } catch (Exception e) {
            throw new UnknownException("Wrong id");
        }
    }
}
