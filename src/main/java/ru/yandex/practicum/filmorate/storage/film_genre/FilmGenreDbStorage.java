package ru.yandex.practicum.filmorate.storage.film_genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SAVE_FILM_GENRES_QUERY = """
            INSERT INTO film_genres (film_id, genre_id)
            VALUES (?, ?)
            """;
    private static final String DELETE_FILM_GENRES_QUERY = """
            DELETE FROM film_genres WHERE film_id = ?
            """;
    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT film_id, genre_id
            FROM film_genres
            WHERE film_id = ?
            """;

    @Override
    public void saveFilmGenres(Long filmId, List<Integer> genreIds) {
        deleteFilmGenres(filmId);

        for (Integer genreId : genreIds) {
            jdbcTemplate.update(SAVE_FILM_GENRES_QUERY, filmId, genreId);
        }
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        jdbcTemplate.update(DELETE_FILM_GENRES_QUERY, filmId);
    }

    @Override
    public List<FilmGenre> findByFilmId(Long filmId) {
        return jdbcTemplate.query(FIND_BY_FILM_ID_QUERY, (rs, rowNum) -> {
            Long filmIdResult = rs.getLong("film_id");
            int genreId = rs.getInt("genre_id");
            return new FilmGenre(filmIdResult, genreId);
        }, filmId);
    }
}
