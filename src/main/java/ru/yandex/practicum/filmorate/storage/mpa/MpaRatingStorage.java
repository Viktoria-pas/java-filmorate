package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

public interface MpaRatingStorage {
    MpaRating save(MpaRating mpaRating);

    Optional<MpaRating> findById(int id);

    Collection<MpaRating> findAll();

    void deleteById(int id);
}
