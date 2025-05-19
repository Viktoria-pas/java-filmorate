package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.Collection;

@Service
public class MpaRatingService {
    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(@Qualifier("mpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public MpaRating save(MpaRating mpaRating) {
        return mpaRatingStorage.save(mpaRating);
    }

    public MpaRating findById(int id) {
        return mpaRatingStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA не найден с id " + id));
    }

    public Collection<MpaRating> findAll() {
        return mpaRatingStorage.findAll();
    }

    public void deleteById(int id) {
        mpaRatingStorage.deleteById(id);
    }
}
