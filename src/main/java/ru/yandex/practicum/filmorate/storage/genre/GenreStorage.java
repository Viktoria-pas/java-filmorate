package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Genre save(Genre genre);

    void deleteGenreById(int id);

    Optional<Genre> findById(int id);

    Collection<Genre> findAll();

}
