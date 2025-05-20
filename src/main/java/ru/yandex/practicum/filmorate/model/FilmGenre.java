package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmGenre {
    private Long filmId;
    private int genreId;

    public FilmGenre(Long filmId, int genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }
}
