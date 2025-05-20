package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();

    @Override
    public void addLike(Long filmId, Long userId) {
        filmLikes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Set<Long> users = filmLikes.get(filmId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                filmLikes.remove(filmId);
            }
        }
    }

    @Override
    public Set<Long> getLikesByFilmId(Long filmId) {
        return filmLikes.getOrDefault(filmId, Collections.emptySet());
    }
}
