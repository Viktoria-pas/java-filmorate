package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAll() {
        log.info("Запрос всех жанров");
        Collection<Genre> genres = genreService.findAll();
        log.debug("Найдено жанров: {}", genres.size());
        return genres;
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable int id) {
        log.info("Запрос жанра с id {}", id);
        Genre genre = genreService.findById(id);
        log.debug("Найден жанр: {}", genre);
        return genre;
    }
}
