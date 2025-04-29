package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос всех фильмов");
        Collection<Film> films = filmService.findAll();
        log.debug("Найдено {} фильмов", films.size());
        return films;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Запрос фильма с ID {}", id);
        Film film = filmService.getById(id);
        log.debug("Возвращаем фильм: {}", film);
        return film;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody @Valid Film film) {
        log.info("Создание нового фильма: {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Фильм создан с ID {}", createdFilm.getId());
        log.debug("Полные данные созданного фильма: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Обновление фильма с ID {}", film.getId());
        Film updatedFilm = filmService.update(film);
        log.info("Фильм с ID {} успешно обновлен", film.getId());
        log.debug("Обновленные данные фильма: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь {} ставит лайк фильму {}", userId, id);
        filmService.addLike(id, userId);
        log.info("Лайк добавлен. Фильм {} теперь имеет {} лайков",
                id, filmService.getById(id).getLikes().size());
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь {} удаляет лайк у фильма {}", userId, id);
        filmService.deleteLike(id, userId);
        log.info("Лайк удален. Фильм {} теперь имеет {} лайков",
                id, filmService.getById(id).getLikes().size());
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") Integer count) {
        log.info("Запрос {} самых популярных фильмов", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.debug("Найдено популярных фильмов: {}", popularFilms.size());
        return popularFilms;
    }
}

