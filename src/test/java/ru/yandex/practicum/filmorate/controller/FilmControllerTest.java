package ru.yandex.practicum.filmorate.controller;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    private final Film film = new Film();

    @Test
    void shouldRejectEmptyName() {
        film.setId(1L);
        film.setName(null);
        film.setDescription("description");
        assertThrows(ConditionsNotMetException.class, () -> filmController.create(film));
    }

    @Test
    void shouldRejectLongDescription() {
        String longDesc = "a".repeat(201);
        film.setDescription(longDesc);
        assertThrows(ConditionsNotMetException.class, () -> filmController.create(film));
    }

    @Test
    void shouldAcceptMaxLengthDescription() {
        String maxDesc = "a".repeat(200);
        film.setName("Film");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        film.setDescription(maxDesc);
        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void shouldRejectEarlyReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ConditionsNotMetException.class, () -> filmController.create(film));
    }

    @Test
    void shouldAcceptCinemaBirthday() {
        film.setName("Film");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void shouldRejectZeroDuration() {
        film.setDuration(10);
        assertThrows(ConditionsNotMetException.class, () -> filmController.create(film));
    }

    @Test
    void shouldAcceptValidFilm() {
        film.setId(1L);
        film.setName("Hobbit");
        film.setDuration(10);
        film.setDescription("The best Film");
        film.setReleaseDate(LocalDate.of(2012, 11, 11));
        assertDoesNotThrow(() -> filmController.create(film));
    }




}
