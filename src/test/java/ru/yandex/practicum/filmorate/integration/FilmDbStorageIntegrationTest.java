package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
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
        UserRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageIntegrationTest {

    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaRatingDbStorage mpaRatingDbStorage;
    private final UserDbStorage userDbStorage;
    private Film testFilm;
    private User testUser;
    private Genre testGenre;
    private MpaRating testMpaRating;

    @BeforeEach
    public void setUp() {
        testMpaRating = mpaRatingDbStorage.findById(1).orElseThrow(() -> new IllegalStateException("MPA not found"));
        testGenre = genreDbStorage.findById(1).orElseThrow(() -> new IllegalStateException("Genre not found"));

        testFilm = new Film();
        testFilm.setName("Integration Test Film");
        testFilm.setDescription("Тестовое описание");
        testFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        testFilm.setDuration(100);
        testFilm.setMpa(testMpaRating);
        testFilm.setGenres(Set.of(testGenre));
        testFilm = filmDbStorage.save(testFilm);

        testUser = new User();
        testUser.setName("Тестовый пользователь");
        testUser.setEmail("test@test.com");
        testUser.setLogin("testuser");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        testUser = userDbStorage.save(testUser);
    }

    @Test
    public void testSaveAndFindFilmById() {

        assertThat(testFilm.getId()).isNotNull();

        Optional<Film> foundFilm = filmDbStorage.findById(testFilm.getId());

        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f.getName()).isEqualTo("Integration Test Film");
                    assertThat(f.getGenres()).extracting("id").contains(testGenre.getId());
                    assertThat(f.getMpa().getId()).isEqualTo(testMpaRating.getId());
                });
    }

    @Test
    public void testUpdateFilm() {
        testFilm.setName("Обновленное название");

        Genre genre2 = genreDbStorage.findById(4)
                .orElseThrow(() -> new IllegalStateException("Genre 'Триллер' not found"));

        testFilm.setGenres(Set.of(genre2));

        Film updatedFilm = filmDbStorage.update(testFilm);

        assertThat(updatedFilm.getName()).isEqualTo("Обновленное название");
        assertThat(updatedFilm.getGenres()).extracting("id").contains(genre2.getId());
    }

    @Test
    public void testDeleteFilm() {

        assertThat(testFilm.getId()).isNotNull();
        filmDbStorage.deleteById(testFilm.getId());
        assertThat(filmDbStorage.findById(testFilm.getId())).isEmpty();
    }

    @Test
    public void testGetPopularFilms() {

        filmDbStorage.addLike(testFilm.getId(), testUser.getId());

        List<Film> popularFilms = filmDbStorage.getPopularFilms(1);

        assertThat(popularFilms).isNotEmpty();
        assertThat(popularFilms.get(0).getId()).isEqualTo(testFilm.getId());
    }
}

