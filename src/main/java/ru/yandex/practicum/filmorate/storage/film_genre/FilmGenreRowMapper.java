package ru.yandex.practicum.filmorate.storage.film_genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {

    @Override
    public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("film_id");
        int genreId = rs.getInt("genre_id");
        return new FilmGenre(filmId, genreId);
    }
}
