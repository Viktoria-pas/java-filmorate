package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос списка всех фильмов. Текущее количество: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен запрос на создание фильма: {}", film);

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан. ID: {}, Название: {}", film.getId(), film.getName());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);

        if (newFilm.getId() == null) {
            log.error("Попытка обновления фильма без ID");
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            log.warn("Фильм с ID {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());

        log.info("Фильм с ID {} успешно обновлен", newFilm.getId());
        return oldFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

