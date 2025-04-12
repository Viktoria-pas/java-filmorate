package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldRejectEmptyName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldRejectLongDescription() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldAcceptMaxLengthDescription() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("a".repeat(200));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectEarlyReleaseDate() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")));
    }

    @Test
    void shouldAcceptCinemaBirthday() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectZeroDuration() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void shouldAcceptValidFilm() {
        Film film = new Film();
        film.setName("Hobbit");
        film.setDescription("The best film");
        film.setReleaseDate(LocalDate.of(2012, 11, 11));
        film.setDuration(150);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}