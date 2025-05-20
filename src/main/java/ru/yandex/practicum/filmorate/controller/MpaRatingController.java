package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final Logger log = LoggerFactory.getLogger(MpaRatingController.class);
    private final MpaRatingService mpaRatingService;

    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public Collection<MpaRating> findAll() {
        log.info("Запрос всех рейтингов MPA");
        Collection<MpaRating> ratings = mpaRatingService.findAll();
        log.debug("Найдено рейтингов: {}", ratings.size());
        return ratings;
    }

    @GetMapping("/{id}")
    public MpaRating findById(@PathVariable int id) {
        log.info("Запрос рейтинга с id {}", id);
        MpaRating rating = mpaRatingService.findById(id);
        log.debug("Найден рейтинг: {}", rating);
        return rating;
    }
}
