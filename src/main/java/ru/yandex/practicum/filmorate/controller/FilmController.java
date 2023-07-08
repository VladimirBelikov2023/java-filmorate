package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getLsFilms() {
        return new ArrayList<>(filmService.getLsFilms());
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody @Valid Film film) {
        return filmService.postFilm(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
    }

    @PutMapping("/films/{idFilm}/like/{idUser}")
    public void addLike(@PathVariable int idFilm, @PathVariable int idUser) {
        filmService.addLike(idUser, idFilm);
    }

    @DeleteMapping("/films/{idFilm}/like/{idUser}")
    public void delLike(@PathVariable int idFilm, @PathVariable int idUser) {
        filmService.delLike(idUser, idFilm);
    }

    @GetMapping("/films/popular")
    public List<Film> getTop(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getPopular(count);
    }


}
