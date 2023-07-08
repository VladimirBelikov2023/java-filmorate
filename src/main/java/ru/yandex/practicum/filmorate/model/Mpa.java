package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Mpa {
    @NotBlank
    private String name;
    private int id;

    public Mpa(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public Mpa() {

    }

}
