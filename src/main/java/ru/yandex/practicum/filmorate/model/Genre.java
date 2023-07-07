package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private String name;
    private int id;

    public Genre(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Genre() {
    }
}
