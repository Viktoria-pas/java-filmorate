package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.util.Collection;
import java.util.Optional;

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
public class MpaDbStorageIntegrationTest {
    private final MpaRatingDbStorage mpaRatingDbStorage;

    @Test
    public void testSaveAndFindMpaRating() {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setName("R");
        mpaRating.setDescription("Restricted");
        MpaRating savedMpa = mpaRatingDbStorage.save(mpaRating);
        assertThat(savedMpa).isNotNull();
        assertThat(savedMpa.getId()).isGreaterThan(0);
        assertThat(savedMpa.getName()).isEqualTo("R");
    }

    @Test
    public void testFindAllMpaRatings() {
        Collection<MpaRating> mpaRatings = mpaRatingDbStorage.findAll();
        assertThat(mpaRatings).isNotNull();
        assertThat(mpaRatings.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testDeleteMpaRatingById() {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setName("NC-17");
        mpaRating.setDescription("Adults Only");
        MpaRating savedMpa = mpaRatingDbStorage.save(mpaRating);
        mpaRatingDbStorage.deleteById(savedMpa.getId());
        Optional<MpaRating> foundMpa = mpaRatingDbStorage.findById(savedMpa.getId());
        assertThat(foundMpa).isEmpty();
    }
}
