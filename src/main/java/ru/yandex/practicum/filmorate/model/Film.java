package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private final String name;
    @Size
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private List<Genre> genres = new ArrayList<>();
    private Integer rate = 0;
    private Mpa mpa = new Mpa();

    private final Set<User> lsLikes = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Mpa getMpa() {
        return mpa;
    }

    public void setMpa(Mpa mpa) {
        this.mpa = mpa;
    }

    public List<Genre> getGenre() {
        return genres;
    }

    public void setGenre(List<Genre> genre) {
        this.genres = genre;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addLike(User user) {
        lsLikes.add(user);
        rate = lsLikes.size();
    }

    public void delLike(User user) {
        lsLikes.remove(user);
        rate = lsLikes.size();
    }

    public Set<User> getLikes() {
        return lsLikes;
    }


}
