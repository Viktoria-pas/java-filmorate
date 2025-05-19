package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryMpaRatingStorage implements MpaRatingStorage {

    private final Map<Integer, MpaRating> ratings = new HashMap<>();
    private int nextId = 1;

    @Override
    public MpaRating save(MpaRating mpaRating) {
        if (mpaRating.getId() == 0) {
            mpaRating.setId(nextId++);
        }
        ratings.put(mpaRating.getId(), mpaRating);
        return mpaRating;
    }

    @Override
    public Optional<MpaRating> findById(int id) {
        return Optional.ofNullable(ratings.get(id));
    }

    @Override
    public Collection<MpaRating> findAll() {
        return ratings.values();
    }

    @Override
    public void deleteById(int id) {
        ratings.remove(id);
    }
}
