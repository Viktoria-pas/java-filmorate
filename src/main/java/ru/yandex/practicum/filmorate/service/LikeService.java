package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

@Service
public class LikeService {

    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public LikeService(
            @Qualifier("likeDbStorage") LikeStorage likeStorage,
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            @Qualifier("userDbStorage") UserStorage userStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        if (userStorage.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден.");
        }

        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {

        if (filmStorage.findById(filmId).isEmpty()) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        if (userStorage.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден.");
        }

        likeStorage.removeLike(filmId, userId);
    }

    public Set<Long> getLikesByFilm(Long filmId) {

        if (filmStorage.findById(filmId).isEmpty()) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        return likeStorage.getLikesByFilmId(filmId);
    }
}