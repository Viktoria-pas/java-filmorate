package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDbStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreDbStorage genreDbStorage;
    private final MpaRatingDbStorage mpaRatingDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaRatingDbStorage mpaRatingDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = new FilmRowMapper(jdbcTemplate);
        this.genreDbStorage = genreDbStorage;
        this.mpaRatingDbStorage = mpaRatingDbStorage;
    }

    private static final String FIND_ALL_QUERY = """
            SELECT f.*, m.id AS mpa_id, m.name AS mpa_name, m.description AS mpa_description
            FROM films f
            JOIN mpa_ratings m ON f.rating_id = m.id
            ORDER BY id ASC
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT f.*, m.id AS mpa_id, m.name AS mpa_name, m.description AS mpa_description
            FROM films f
            JOIN mpa_ratings m ON f.rating_id = m.id
            WHERE f.id = ?
            """;
    private static final String SAVE_QUERY = ("INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)");
    private static final String UPDATE_QUERY = ("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?  WHERE id = ?");
    private static final String DELETE_BY_ID_QUERY = ("DELETE FROM films WHERE id = ?");
    private static final String ADD_LIKES_QUERY = ("MERGE INTO likes KEY(film_id, user_id) VALUES (?, ?)");
    private static final String DELETE_LIKES_QUERY = ("DELETE FROM likes WHERE film_id = ? AND user_id = ?");
    private static final String GET_POPULAR_FILMS_QUERY = """
            SELECT f.*, m.id AS mpa_id, m.name AS mpa_name, m.description AS mpa_description, COUNT(l.user_id) AS likes_count
            FROM films f
            LEFT JOIN likes l ON f.id = l.film_id
            JOIN mpa_ratings m ON f.rating_id = m.id
            GROUP BY f.id
            ORDER BY likes_count DESC
            LIMIT ?
            """;

    @Override
    public Film save(Film film) {
        jdbcTemplate.update(SAVE_QUERY, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        String sql = "SELECT MAX(id) FROM films";
        Long filmId = jdbcTemplate.queryForObject(sql, Long.class);
        film.setId(filmId);
        List<Integer> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .toList();
        genreDbStorage.saveGenres(filmId, genreIds);
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(UPDATE_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        genreDbStorage.deleteGenresByFilmId(film.getId());
        List<Integer> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .toList();
        genreDbStorage.saveGenres(film.getId(), genreIds);
        return film;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, filmRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, filmRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        genreDbStorage.deleteGenresByFilmId(id);
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(ADD_LIKES_QUERY, filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update(DELETE_LIKES_QUERY, filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = jdbcTemplate.query(GET_POPULAR_FILMS_QUERY, filmRowMapper, count);
        popularFilms.forEach(film -> {
            List<Genre> genreList = genreDbStorage.findGenresByFilmId(film.getId());
            film.setGenres(new HashSet<>(genreList));
        });
        return popularFilms;
    }
}
