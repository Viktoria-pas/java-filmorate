package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    void addFriendship(Long userId, Long friendId);

    void updateFriendship(Friendship friendship);

    void removeFriendship(Long userId, Long friendId);

    List<Friendship> findFriendshipsByUserId(Long userId);
}
