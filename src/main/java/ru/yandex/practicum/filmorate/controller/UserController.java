package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос всех пользователей");
        Collection<User> users = userService.findAll();
        log.debug("Найдено {} пользователей", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Запрос пользователя с ID {}", id);
        User user = userService.getById(id);
        log.debug("Возвращаем пользователя: {}", user);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid User user) {
        log.info("Создание нового пользователя: {}", user);
        User createdUser = userService.save(user);
        log.info("Пользователь создан с ID {}", createdUser.getId());
        log.debug("Полные данные созданного пользователя: {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Обновление пользователя с ID {}", user.getId());
        User updatedUser = userService.update(user);
        log.info("Пользователь с ID {} успешно обновлен", user.getId());
        log.debug("Обновленные данные пользователя: {}", updatedUser);
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователь {} добавляет в друзья пользователя {}", id, friendId);
        friendshipService.addFriend(id, friendId);
        log.info("Пользователи {} и {} теперь друзья", id, friendId);
    }

    @GetMapping("/{id}/friends/requests")
    public List<User> getFriendRequests(@PathVariable Long id) {
        log.info("Запрос входящих заявок в друзья для пользователя {}", id);
        return friendshipService.getFriendRequests(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователь {} удаляет из друзей пользователя {}", id, friendId);
        friendshipService.removeFriend(id, friendId);
        log.info("Пользователи {} и {} больше не друзья", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Запрос списка друзей пользователя {}", id);
        List<User> friends = friendshipService.getFriends(id);
        log.debug("Найдено {} друзей пользователя {}", friends.size(), id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Запрос общих друзей пользователей {} и {}", id, otherId);
        List<User> commonFriends = friendshipService.getCommonFriends(id, otherId);
        log.debug("Найдено {} общих друзей", commonFriends.size());
        return commonFriends;
    }
}

