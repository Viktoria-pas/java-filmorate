package ru.yandex.practicum.filmorate.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос списка всех пользователей. Текущее количество: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);

        try {
            validateUser(user);
            user.setId(getNextId());
            if (user.getName() == null || user.getName().isBlank()) {
                log.debug("Имя пользователя не указано, используем логин: {}", user.getLogin());
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь успешно создан. ID: {}, Email: {}", user.getId(), user.getEmail());
            return user;
        } catch (ConditionsNotMetException e) {
            log.warn("Ошибка валидации при создании пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);

        if (newUser.getId() == null) {
            log.error("Попытка обновления пользователя без ID");
            throw new ConditionsNotMetException("Id не может быть пустым");
        }
        User oldUser = users.get(newUser.getId());
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с ID {} не найден", newUser.getId());
            throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
        }
        try {
            validateUser(newUser);
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
        } catch (ConditionsNotMetException e) {
            log.warn("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Валидация не пройдена: email {} не содержит @", user.getEmail());
            throw new ConditionsNotMetException("Email должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ConditionsNotMetException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Валидация не пройдена: логин {} содержит пробелы", user.getLogin());
            throw new ConditionsNotMetException("Логин не должен содержать пробелов");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Валидация не пройдена: некорректная дата рождения {}", user.getBirthday());
            throw new ConditionsNotMetException("Дата рождения указана некорректно");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Сгенерирован новый ID: {}", currentMaxId);
        return ++currentMaxId;
    }
}

