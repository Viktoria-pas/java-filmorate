package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Autowired
    public FriendshipService(FriendshipStorage friendshipStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.friendshipStorage = friendshipStorage;
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        validateUser(userId);
        validateUser(friendId);

        try {
            friendshipStorage.addFriendship(userId, friendId);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Friendship request already exists");
        }
    }

    public List<User> getFriends(Long userId) {
        validateUser(userId);
        return friendshipStorage.findFriendshipsByUserId(userId).stream()
                .filter(f -> f.getStatus().isConfirmed())  // Фильтруем только подтвержденные дружбы
                .map(f -> f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId())
                .map(id -> userStorage.findById(id).orElseThrow(() ->
                        new NotFoundException("Пользователь с ID " + id + " не найден")))
                .collect(Collectors.toList());
    }

    public void removeFriend(Long userId, Long friendId) {
        validateUser(userId);
        validateUser(friendId);
        try {
            friendshipStorage.removeFriendship(userId, friendId);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Error removing friendship", e);
        }
    }


    public List<User> getFriendRequests(Long userId) {
        validateUser(userId);
        return friendshipStorage.findFriendshipsByUserId(userId).stream()
                .filter(f -> f.getStatus() == FriendshipStatus.PENDING)
                .map(f -> userStorage.findById(f.getFriendId()).orElseThrow(() ->
                        new NotFoundException("Пользователь с ID " + f.getFriendId() + " не найден")))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        validateUser(userId);
        validateUser(otherUserId);
        List<User> userFriends = getFriends(userId);
        List<User> otherUserFriends = getFriends(otherUserId);

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toList());
    }

    private void validateUser(Long userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }
}
