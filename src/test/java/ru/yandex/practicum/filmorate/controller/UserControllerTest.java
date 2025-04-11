package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;
    private final User user = new User();

    @Test
    void shouldRejectEmptyEmail() {
        user.setEmail("");
        assertThrows(ConditionsNotMetException.class, () -> userController.create(user));
    }

    @Test
    void shouldRejectEmailWithoutAt() {
        user.setEmail("invalid.email");
        assertThrows(ConditionsNotMetException.class, () -> userController.create(user));
    }

    @Test
    void shouldRejectEmptyLogin() {
        user.setLogin("");
        assertThrows(ConditionsNotMetException.class, () -> userController.create(user));
    }

    @Test
    void shouldRejectLoginWithSpaces() {
        user.setLogin("login with spaces");
        assertThrows(ConditionsNotMetException.class, () -> userController.create(user));
    }

    @Test
    void shouldUseLoginWhenNameEmpty() {
        user.setLogin("123");
        user.setEmail("email@post.com");
        user.setBirthday(LocalDate.of(1995, 12, 15));
        User created = userController.create(user);
        assertEquals(user.getLogin(), created.getName());
    }

    @Test
    void shouldRejectFutureBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ConditionsNotMetException.class, () -> userController.create(user));
    }

    @Test
    void shouldAcceptCurrentDateBirthday() {
        user.setEmail("email@post.com");
        user.setBirthday(LocalDate.now());
        user.setLogin("123");
        assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    void shouldAcceptValidUser() {
        user.setId(1L);
        user.setName("Vova");
        user.setEmail("email@post.com");
        user.setLogin("vava123");
        user.setBirthday(LocalDate.of(1995, 12, 15));
        assertDoesNotThrow(() -> userController.create(user));
    }
}
