package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;


    @Test
    public void testAddUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.postUser(new User(1, "sdsd22@mail.ru", "dssd22", "vovan", LocalDate.of(2005, 11, 2))));
        User user1 = userStorage.getLsUsers().get(0);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
        List<User> ls = userStorage.getLsUsers();
        assertEquals(userOptional.get(), user1, "Users не совпадают");
    }


    @Test
    public void testFindUserById() {
        userStorage.postUser(new User(2, "sdsd22@mail.ru", "dssd22", "vovan", LocalDate.of(2005, 11, 2)));
        Optional<User> userOptional = Optional.of(userStorage.getUser(2));
        assertEquals(0, 1, userStorage.getLsUsers().get(0).getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
    }

    @Test
    public void testFindUsers() {

        List<User> ls = userStorage.getLsUsers();

        assertEquals(ls.size(), 2, "Размер списка не совпадает");
    }


    @Test
    public void testUpdateUser() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(new User(1, "sdsd22@mail.ru", "msc", "Igor", LocalDate.of(2005, 11, 2))));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "msc").hasFieldOrPropertyWithValue("name", "Igor")
                );
    }


    @Test
    public void testAddFilmById() {
        Film film = new Film(2, "Volk", "dssd22", LocalDate.of(2000, 12, 1), 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.postFilm(film));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 3)
                );
        assertEquals(filmOptional.get(), film, "Films не совпадают");
    }

    @Test
    public void testFindFilmById() {
        Film film1 = new Film(2, "Volk", "dssd22", LocalDate.of(2000, 12, 1), 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film1.setMpa(mpa);
        filmDbStorage.postFilm(film1);

        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.getLsFilms().get(0));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindFilms() {

        List<Film> ls = filmDbStorage.getLsFilms();

        assertEquals(ls.size(), 2, "Размер списка не совпадает");
    }

    @Test
    public void testUpdateFilm() {
        Film film1 = new Film(1, "Russia", "dssd22", LocalDate.of(2000, 12, 1), 10);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film1.setMpa(mpa);
        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.update(film1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Russia").hasFieldOrPropertyWithValue("duration", 10)
                );
    }


    @Test
    public void testAddLike() {
        userStorage.postUser(new User(2, "sdsd2wwwww@mail.ru", "dsssssd22", "vovan", LocalDate.of(2005, 11, 2)));
        Film film1 = new Film(2, "Volk", "dssd22", LocalDate.of(2000, 12, 1), 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film1.setMpa(mpa);
        filmDbStorage.postFilm(film1);
        filmDbStorage.addLike(userStorage.getLsUsers().get(0).getId(), userStorage.getLsUsers().get(0).getId());

        Optional<Film> film = Optional.ofNullable(filmDbStorage.getLsFilms().get(0));

        assertEquals(1, film.get().getRate());

    }


    @Test
    public void testDelLike() {
        filmDbStorage.delLike(1, 1);

        Optional<Film> film = Optional.ofNullable(filmDbStorage.getFilm(1));

        assertEquals(0, film.get().getRate());

    }


    @Test
    public void testDelUserById() {

        userStorage.deleteUser(1);

        assertEquals(userStorage.getLsUsers().size(), 1, "Пользователь не удалился");
    }


    @Test
    public void testFindGenreById() {

        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenre(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void testFindGenre() {

        List<Genre> ls = genreStorage.getLsGenre();

        Optional<Genre> genre = Optional.ofNullable(ls.get(0));

        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Комедия")
                );
        assertEquals(6, ls.size());
    }

    @Test
    public void testFindMpaById() {

        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpa(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void testFindMpa() {

        List<Mpa> ls = mpaStorage.getLsMpa();

        Optional<Mpa> genre = Optional.ofNullable(ls.get(0));

        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "G")
                );
        assertEquals(5, ls.size());
    }

}