package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getLsMpa() {
        return jdbcTemplate.query("select * from rating", mpaRowMapper());
    }

    @Override
    public Mpa getMpa(int id) {
        return jdbcTemplate.queryForObject("select * from rating where rating_id = ?", mpaRowMapper(), id);
    }


    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> {
            Mpa mpa = new Mpa(
                    rs.getString("name"),
                    rs.getInt("rating_id")
            );
            return mpa;
        };
    }
}
