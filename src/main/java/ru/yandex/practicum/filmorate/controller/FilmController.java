package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос списка всех фильмов. Текущее количество: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);

        try {
            validateFilm(film);
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Фильм успешно создан. ID: {}, Название: {}", film.getId(), film.getName());
            return film;
        } catch (ConditionsNotMetException e) {
            log.warn("Ошибка валидации при создании фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);

        if (newFilm.getId() == null) {
            log.error("Попытка обновления фильма без ID");
            throw new ConditionsNotMetException("Id не может быть пустым");
        }

        Film oldFilm = films.get(newFilm.getId());
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм с ID {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        try {
            validateFilm(newFilm);
            log.debug("Обновление данных фильма. Старые данные: {}, Новые данные: {}", oldFilm, newFilm);

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());

            log.info("Фильм с ID {} успешно обновлен", newFilm.getId());

            return oldFilm;
        } catch (ConditionsNotMetException e) {
            log.warn("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Валидация не пройдена: описание фильма слишком длинное ({} символов)",
                    film.getDescription().length());
            throw new ConditionsNotMetException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.warn("Валидация не пройдена: некорректная дата релиза {}", film.getReleaseDate());
            throw new ConditionsNotMetException("Дата релиза фильма может быть не познее 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.warn("Валидация не пройдена: некорректная продолжительность {}", film.getDuration());
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительной");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Сгенерирован новый ID: {}", currentMaxId);
        return ++currentMaxId;
    }
}

