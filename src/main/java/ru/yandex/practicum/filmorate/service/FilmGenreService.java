package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreStorage;

import java.util.List;

@Service
public class FilmGenreService {

    private final FilmGenreStorage filmGenreStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmGenreService(
            @Qualifier("filmGenreDbStorage") FilmGenreStorage filmGenreStorage,
            @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmGenreStorage = filmGenreStorage;
        this.filmStorage = filmStorage;
    }

    public void saveGenresForFilm(Long filmId, List<Integer> genreIds) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        filmGenreStorage.saveFilmGenres(filmId, genreIds);
    }

    public List<FilmGenre> getGenresForFilm(Long filmId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        return filmGenreStorage.findByFilmId(filmId);
    }

    public void deleteGenresForFilm(Long filmId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        filmGenreStorage.deleteFilmGenres(filmId);
    }
}
