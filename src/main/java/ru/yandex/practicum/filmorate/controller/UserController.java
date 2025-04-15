package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос списка всех пользователей. Текущее количество: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен запрос на создание пользователя: {}", user);

        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не указано, используем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. ID: {}, Email: {}", user.getId(), user.getEmail());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);

        if (newUser.getId() == null) {
            log.error("Попытка обновления пользователя без ID");
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.warn("Пользователь с ID {} не найден", newUser.getId());
            throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
        }

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            log.debug("Имя пользователя не указано, используем логин: {}", newUser.getLogin());
            newUser.setName(newUser.getLogin());
        }

        log.debug("Обновление данных пользователя. Старые данные: {}, Новые данные: {}", oldUser, newUser);
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());

        log.info("Пользователь с ID {} успешно обновлен", newUser.getId());
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

