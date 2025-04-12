package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<User>> validateUser(User user) {
        return validator.validate(user);
    }

    @Test
    void shouldRejectEmptyEmail() {
        User user = new User();
        user.setEmail("");
        user.setLogin("user123");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectEmailWithoutAtSymbol() {
        User user = new User();
        user.setEmail("invalid.email");
        user.setLogin("user123");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectEmptyLogin() {
        User user = new User();
        user.setEmail("email@post.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptUserWhenNameIsNull() {
        User user = new User();
        user.setEmail("email@post.com");
        user.setLogin("user123");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName(null);

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectFutureBirthday() {
        User user = new User();
        user.setEmail("email@post.com");
        user.setLogin("user123");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptTodayBirthday() {
        User user = new User();
        user.setEmail("email@post.com");
        user.setLogin("user123");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptValidUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@post.com");
        user.setLogin("user123");
        user.setName("Vova");
        user.setBirthday(LocalDate.of(1995, 12, 15));

        Set<ConstraintViolation<User>> violations = validateUser(user);
        assertTrue(violations.isEmpty());
    }
}