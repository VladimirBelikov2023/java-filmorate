package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class GenreController {
    GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getLsGenres() {
        List<Genre> ls = new ArrayList<>(genreService.getLsGenre());
        return ls;
    }


    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        return genreService.getGenre(id);
    }
}
