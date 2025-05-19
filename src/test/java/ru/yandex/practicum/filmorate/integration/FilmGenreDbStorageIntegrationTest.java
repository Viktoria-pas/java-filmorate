package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        FilmDbStorage.class,
        GenreDbStorage.class,
        MpaRatingDbStorage.class,
        GenreRowMapper.class,
        MpaRatingRowMapper.class,
        FilmRowMapper.class,
        UserDbStorage.class,
        UserRowMapper.class,
        FilmGenreDbStorage.class,
        FilmGenreRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDbStorageIntegrationTest {
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaRatingDbStorage mpaRatingDbStorage;
    private final UserDbStorage userDbStorage;
    private Film testFilm;
    private User testUser;
    private Genre testGenre;
    private Genre testGenre2;
    private MpaRating testMpaRating;

    @BeforeEach
    public void setUp() {
        testMpaRating = mpaRatingDbStorage.findById(1)
                .orElseGet(() -> {
                    MpaRating newMpa = new MpaRating();
                    newMpa.setName("G");
                    newMpa.setDescription("General audiences");
                    return mpaRatingDbStorage.save(newMpa);
                });

        testGenre = genreDbStorage.findById(1)
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName("Комедия");
                    return genreDbStorage.save(newGenre);
                });

        testGenre2 = genreDbStorage.findById(2)
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName("Драма");
                    return genreDbStorage.save(newGenre);
                });

        testFilm = new Film();
        testFilm.setName("Integration Test Film");
        testFilm.setDescription("Тестовое описание");
        testFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        testFilm.setDuration(100);
        testFilm.setMpa(testMpaRating);
        testFilm.setGenres(Set.of(testGenre, testGenre2));
        testFilm = filmDbStorage.save(testFilm);

        testUser = new User();
        testUser.setName("Тестовый пользователь");
        testUser.setEmail("test@test.com");
        testUser.setLogin("testuser");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        testUser = userDbStorage.save(testUser);
    }


    @Test
    public void testSaveAndFindFilmGenres() {

        filmGenreDbStorage.saveFilmGenres(testFilm.getId(), List.of(testGenre.getId(), testGenre2.getId()));
        List<FilmGenre> genres = filmGenreDbStorage.findByFilmId(testFilm.getId());
        assertThat(genres).isNotNull();
        assertThat(genres.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testDeleteFilmGenres() {
        filmGenreDbStorage.saveFilmGenres(testFilm.getId(), List.of(testGenre.getId(), testGenre2.getId()));
        filmGenreDbStorage.deleteFilmGenres(testFilm.getId());
        List<FilmGenre> genres = filmGenreDbStorage.findByFilmId(testFilm.getId());
        assertThat(genres).isEmpty();
    }
}

