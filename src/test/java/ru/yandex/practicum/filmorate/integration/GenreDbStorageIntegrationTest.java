package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({GenreDbStorage.class, GenreRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageIntegrationTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void testSaveAndFindGenre() {
        Genre genre = new Genre();
        genre.setName("Комедия");
        Genre savedGenre = genreDbStorage.save(genre);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isGreaterThan(0);
        assertThat(savedGenre.getName()).isEqualTo("Комедия");
    }

    @Test
    public void testFindAllGenres() {
        List<Genre> genres = genreDbStorage.findAll();
        assertThat(genres).isNotNull();
        assertThat(genres.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testDeleteGenreById() {
        Genre genre = new Genre();
        genre.setName("Драма");
        Genre savedGenre = genreDbStorage.save(genre);

        genreDbStorage.deleteGenreById(savedGenre.getId());

        Optional<Genre> foundGenre = genreDbStorage.findById(savedGenre.getId());
        assertThat(foundGenre).isEmpty();
    }
}
