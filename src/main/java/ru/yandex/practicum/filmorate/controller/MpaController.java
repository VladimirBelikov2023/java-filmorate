package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class MpaController {

    MpaService mpaService;

    @GetMapping("/mpa")
    public List<Mpa> getLsMpa() {
        List<Mpa> ls = mpaService.getLsMpa();
        return ls;
    }


    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return mpaService.getMpa(id);
    }
}
