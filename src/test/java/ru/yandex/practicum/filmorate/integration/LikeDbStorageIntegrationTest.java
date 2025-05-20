package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        LikeDbStorage.class,
        LikeRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageIntegrationTest {

    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaRatingDbStorage mpaRatingDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;
    private Film testFilm;
    private User testUser;
    private Genre testGenre;
    private MpaRating testMpaRating;

    @BeforeEach
    public void setUp() {
        Optional<MpaRating> mpaOptional = mpaRatingDbStorage.findById(1);
        testMpaRating = mpaRatingDbStorage.findById(1)
                .orElseGet(() -> {
                    MpaRating newMpa = new MpaRating();
                    newMpa.setId(1);
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
    public void testAddLike() {
        likeDbStorage.addLike(testFilm.getId(), testUser.getId());
        Set<Long> likes = likeDbStorage.getLikesByFilmId(testFilm.getId());
        assertThat(likes).contains(testUser.getId());
    }

    @Test
    public void testRemoveLike() {
        likeDbStorage.addLike(testFilm.getId(), testUser.getId());
        likeDbStorage.removeLike(testFilm.getId(), testUser.getId());
        Set<Long> likes = likeDbStorage.getLikesByFilmId(testFilm.getId());
        assertThat(likes).doesNotContain(testUser.getId());
    }

    @Test
    public void testGetLikesByFilmId() {
        likeDbStorage.addLike(testFilm.getId(), testUser.getId());
        Set<Long> likes = likeDbStorage.getLikesByFilmId(testFilm.getId());
        assertThat(likes).containsExactly(testUser.getId());
    }

    @Test
    public void testAddLikeToNonExistentFilm() {
        assertThrows(DataIntegrityViolationException.class, () -> likeDbStorage.addLike(9999L, testUser.getId()));
    }

    @Test
    public void testAddLikeFromNonExistentUser() {
        assertThrows(DataIntegrityViolationException.class, () -> likeDbStorage.addLike(testFilm.getId(), 9999L));
    }
}
