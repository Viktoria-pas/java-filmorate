package ru.yandex.practicum.filmorate.storage.film_genre;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {
    void saveFilmGenres(Long filmId, List<Integer> genreIds); // Сохранение связей фильм-genre

    void deleteFilmGenres(Long filmId); // Удаление всех жанров для фильма

    List<FilmGenre> findByFilmId(Long filmId); // Поиск всех жанров для фильма
}