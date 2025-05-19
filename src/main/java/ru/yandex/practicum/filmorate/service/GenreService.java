package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> findAll() {
        Collection<Genre> genres = genreStorage.findAll();
        return genres;
    }

    public Genre findById(int id) {
        if (id <= 0) {
            throw new NotFoundException("ID жанра должен быть положительным числом");
        }

        Genre genre = genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с ID " + id + " не найден"));

        return genre;
    }

    public Genre save(Genre genre) {
        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название жанра не должно быть пустым");
        }

        Genre savedGenre = genreStorage.save(genre);
        return savedGenre;
    }

    public Genre update(Genre genre) {
        if (genre.getId() <= 0) {
            throw new NotFoundException("ID жанра должен быть положительным числом");
        }

        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название жанра не должно быть пустым");
        }

        Genre updatedGenre = genreStorage.save(genre);
        return updatedGenre;
    }

    public void deleteById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID жанра должен быть положительным числом");
        }

        genreStorage.deleteGenreById(id);
    }
}
