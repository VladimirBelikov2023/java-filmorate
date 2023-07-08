package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getLsGenre() {
        return jdbcTemplate.query("select * from genre_name", genreRowMapper());
    }

    @Override
    public Genre getGenre(int id) {
        return jdbcTemplate.queryForObject("select * from genre_name where genre_id = ?", genreRowMapper(), id);
    }


    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> {
            Genre genre = new Genre(
                    rs.getString("genre_name"),
                    rs.getInt("genre_id")
            );
            return genre;
        };
    }
}
