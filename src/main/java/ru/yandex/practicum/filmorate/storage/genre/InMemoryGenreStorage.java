package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
public class InMemoryGenreStorage implements GenreStorage {
    private final Map<Integer, Genre> genres = new HashMap<>();

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            genre.setId(getNextId());
        }

        genres.put(genre.getId(), genre);
        return genre;
    }

    @Override
    public Optional<Genre> findById(int id) {
        return Optional.ofNullable(genres.get(id));
    }

    @Override
    public Collection<Genre> findAll() {
        return genres.values();
    }

    @Override
    public void deleteGenreById(int id) {
        genres.remove(id);
    }

    private int getNextId() {
        int currentMaxId = genres.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
