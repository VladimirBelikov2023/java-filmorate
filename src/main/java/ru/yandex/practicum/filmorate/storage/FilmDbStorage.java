package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {
    private Integer k;
    Map<Integer, Film> ls = new HashMap<>();

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(Map<Integer, Film> ls, JdbcTemplate jdbcTemplate) {
        this.k = 1;
        this.ls = ls;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override

    public List<Film> getLsFilms() {
        ls.clear();
        List<Film> ls = jdbcTemplate.queryForObject("select f.id as id_film,f.rating_id, f.name as film_name , f.description, f.release_date, f.duration, g.genre_id, gn.genre_name , r.name as rating, u.id as id_user , u.email , u.login , u.name as name_user, u.birthday  \n" +
                "                from films f \n" +
                "                 left join genre g on g.film_id = f.id " +
                "                 left join genre_name gn on gn.genre_id = g.genre_id " +
                "                  left join rating r on r.rating_id =f.rating_id \n" +
                "                left join ls_likes ll on ll.film_id = f.id " +
                "               left join users as u on u.id = ll.user_id order by f.id", filmRowMapper());
        return ls;
    }

    @Override
    public Film postFilm(Film film) {

        Set<Genre> ls;
        if (!film.getGenre().isEmpty()) {
            ls = film.getGenre();
        } else {
            ls = new HashSet<>();
        }
        Mpa mpa = film.getMpa();

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params;
        if (ls.isEmpty()) {
            params = Map.of("name", film.getName(), "description", film.getDescription(),
                    "release_date", film.getReleaseDate().toString(), "duration", String.valueOf(film.getDuration()), "rating_id", mpa.getId());
        } else if (mpa == null) {
            params = Map.of("name", film.getName(), "description", film.getDescription(),
                    "release_date", film.getReleaseDate().toString(), "duration", String.valueOf(film.getDuration()));
            addGenreCh(ls, k);
        } else {
            params = Map.of("name", film.getName(), "description", film.getDescription(),
                    "release_date", film.getReleaseDate().toString(), "duration", String.valueOf(film.getDuration()),
                    "rating_id", mpa.getId());
        }

        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());
        addGenreCh(ls, film.getId());
        k += 1;
        return film;

    }

    @Override
    public Film update(Film film) {
        Set<Genre> ls;
        if (!film.getGenre().isEmpty()) {
            ls = film.getGenre();
        } else {
            ls = new HashSet<>();
        }
        if (film.getGenre().isEmpty() && film.getMpa() == null) {
            jdbcTemplate.update("update films set name =?, description = ?, release_date = ?," +
                            "duration = ?   where id = ?", film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getId());
            return film;
        } else if (film.getMpa() == null) {
            jdbcTemplate.update("update films set name =?, description = ?, release_date = ?," +
                            "duration = ?, rating_id= ?  where id = ?", film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), null, film.getId());
            addGenreCh(ls, film.getId());
            return getFilm(film.getId());
        } else if (film.getGenre().isEmpty()) {
            jdbcTemplate.update("update films set name =?, description = ?, release_date = ?," +
                            "duration = ?,  rating_id= ?  where id = ?", film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
            addGenreCh(new HashSet<>(), film.getId());
            return film;
        } else {
            jdbcTemplate.update("update films set name =?, description = ?, release_date = ?," +
                            "duration = ?, rating_id= ?  where id = ?", film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
            addGenreCh(ls, film.getId());
            return getFilm(film.getId());
        }
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update("delete from films where id = ?", id);
    }

    @Override
    public Film getFilm(int id) {
        ls.clear();
        List<Film> ls = jdbcTemplate.queryForObject("select f.id as id_film, f.rating_id, f.name as film_name , f.description, f.release_date, f.duration, g.genre_id , gn.genre_name ,  r.name as rating, u.id as id_user , u.email , u.login , u.name as name_user , u.birthday \n" +
                "                from films f \n" +
                "                left join genre g on g.film_id = f.id left join genre_name gn on gn.genre_id = g.genre_id  left join rating r on r.rating_id =f.rating_id \n" +
                "                left join ls_likes ll on ll.film_id = f.id left join users u on u.id = ll.user_id where f.id = ?", filmRowMapper(), id);

        if (ls == null) {
            throw new ValidateException("Wrong id");
        }
        return ls.get(0);
    }

    @Override
    public void addLike(int idUser, int idFilm) {
        jdbcTemplate.update("INSERT INTO ls_likes (user_id, film_id ) VALUES(?,?)", idUser, idFilm);
    }

    @Override
    public void delLike(int idUser, int idFilm) {
        jdbcTemplate.update("delete from ls_likes where user_id = ? and film_id =?", idUser, idFilm);

    }

    private RowMapper<List<Film>> filmRowMapper() {
        return (rs, rowNum) -> {
            HashMap<Integer, Set<Genre>> gn = new HashMap<>();
            do {
                int id = rs.getInt("id_film");
                if (!ls.containsKey(rs.getInt("id_film"))) {
                    Film film = new Film(
                            rs.getInt("id_film"),
                            rs.getString("film_name"),
                            rs.getString("description"),
                            rs.getDate("release_date").toLocalDate(),
                            rs.getInt("duration")
                    );
                    HashMap<String, Integer> mp = new HashMap<>();
                    Mpa mpa = new Mpa();
                    mpa.setId(rs.getInt("rating_id"));
                    mpa.setName(rs.getString("rating"));
                    film.setMpa(mpa);
                    film.setRate(film.getLikes().size());
                    ls.put(film.getId(), film);
                }
                if (ls.containsKey(rs.getInt("id_film")) && rs.getString("email") != null) {
                    User user = new User(
                            rs.getInt("id_user"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name_user"),
                            rs.getDate("birthday").toLocalDate());
                    ls.get(rs.getInt("id_film")).addLike(user);
                }
                Genre genre = new Genre();
                if (ls.containsKey(rs.getInt("id_film")) && rs.getString("genre_id") != null) {
                    genre.setId(rs.getInt("genre_id"));
                    genre.setName(rs.getString("genre_name"));
                    Set<Genre> lss;
                    if (gn.containsKey(id)) {
                        lss = gn.get(id);
                    } else {
                        lss = new HashSet<>();
                    }
                    if (!lss.contains(genre)) {
                        lss.add(genre);
                    }
                    List<Genre> s = new ArrayList<>(lss);
                    s.sort(new Comparator<Genre>() {
                        @Override
                        public int compare(Genre o1, Genre o2) {
                            return o1.getId() - o2.getId();
                        }
                    });
                    lss = new HashSet<>(s);
                    ls.get(id).setGenre(lss);
                    gn.put(id, lss);

                } else {
                    Set<Genre> lss;
                    if (gn.containsKey(id)) {
                        lss = gn.get(id);
                    } else {
                        lss = new HashSet<>();
                    }
                    lss.add(genre);
                    ls.get(id).setGenre(lss);
                    gn.put(id, lss);
                    if (genre.getId() == 0) {
                        ls.get(id).setGenre(new HashSet<>());
                    }
                }
            } while (rs.next());
            List<Film> ls1 = new ArrayList<>();
            for (Film o : ls.values()) {
                ls1.add(o);
            }
            return ls1;
        };
    }

    public List<Film> getPopular(int id) {
        ls.clear();

        List<Film> f = jdbcTemplate.queryForObject("select f.id as id_film, f.name as film_name, f.description , f.release_date , f.duration , f.rating_id  ,max(r.name) as rating, max(u.id) as id_user, max(u.email) as email, max(u.login) as login, max(u.name) as name_user, max(u.birthday) as birthday, max (g.genre_id) as genre_id, max(gn.genre_name) as genre_name  ,count(ll.user_id) \n" +
                "                from films f left join ls_likes ll on ll.film_id = f.id \n" +
                "                left join users u on u.id =ll.user_id left join genre g ON g.film_id = f.id left join genre_name gn on gn.genre_id =g.genre_id left join rating r on r.rating_id =f.rating_id  group by f.id \n" +
                "                 order by count(ll.user_id) desc limit ?", filmRowMapper(), id);
        if (f == null) {
            return new ArrayList<>();
        }
        List<Film> ans = new ArrayList<>();
        for (Film o : getLsFilms()) {
            for (Film o1 : f) {
                if (o1.getId() == o.getId()) {
                    ans.add(o);
                }
            }
        }
        return ans;
    }

    private void addGenreCh(Set<Genre> ls, int id) {
        jdbcTemplate.update("delete from genre where film_id = ?", id);
        if (ls.isEmpty()) {
            return;
        }
        List<Genre> ll = new ArrayList<>(ls);
        for (int i = 0; i < ls.size(); i++) {
            jdbcTemplate.update("INSERT INTO genre  (film_id," +
                    " genre_id) VALUES(?,?) ", id, ll.get(i).getId());
        }
    }

}
