package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private int id;
    private String name;

    public Genre(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Genre() {
    }
}
