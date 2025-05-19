package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;
    private static final String FIND_ALL_QUERY = ("SELECT * FROM genres");
    private static final String FIND_BY_ID_QUERY = ("SELECT * FROM genres WHERE id = ?");
    private static final String SAVE_QUERY = ("INSERT INTO genres (name) VALUES (?)");
    private static final String DELETE_BY_ID_QUERY = ("DELETE FROM genres WHERE id = ?");
    private static final String SAVE_GENRES_QUERY = ("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)");
    private static final String DELETE_GENRES_BY_FILM_ID_QUERY = ("DELETE FROM film_genres WHERE film_id = ?");
    private static final String FIND_GENRES_BY_FILMID = ("SELECT g.* FROM genres g " +
            "JOIN film_genres fg ON g.id = fg.genre_id " +
            "WHERE fg.film_id = ?");

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper genreRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public Genre save(Genre genre) {
        jdbcTemplate.update(SAVE_QUERY, genre.getName());
        String sql = "SELECT MAX(id) FROM genres";
        int genreId = jdbcTemplate.queryForObject(sql, Integer.class);
        genre.setId(genreId);
        return genre;
    }

    @Override
    public Optional<Genre> findById(int id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, genreRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, genreRowMapper);
    }

    public void deleteGenreById(int genreId) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, genreId);
    }

    public void saveGenres(Long filmId, List<Integer> genreIds) {
        for (Integer genreId : genreIds) {
            jdbcTemplate.update(SAVE_GENRES_QUERY, filmId, genreId);
        }
    }

    public void deleteGenresByFilmId(Long filmId) {
        jdbcTemplate.update(DELETE_GENRES_BY_FILM_ID_QUERY, filmId);
    }

    public List<Genre> findGenresByFilmId(Long filmId) {
        return jdbcTemplate.query(FIND_GENRES_BY_FILMID, genreRowMapper, filmId);
    }

}
