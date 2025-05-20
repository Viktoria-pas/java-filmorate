package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FilmRowMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;

    public FilmRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        MpaRating mpa = new MpaRating();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa_name"));
        mpa.setDescription(rs.getString("mpa_description"));
        film.setMpa(mpa);

        film.setGenres(getGenres(film.getId()));
        return film;
    }

    private Set<Genre> getGenres(Long filmId) throws SQLException {
        String sql = """
                SELECT g.id, g.name FROM genres g
                JOIN film_genres fg ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;
        return new HashSet<>(jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    Genre genre = new Genre();
                    genre.setId(rs.getInt("id"));
                    genre.setName(rs.getString("name"));
                    return genre;
                }, filmId));
    }
}
