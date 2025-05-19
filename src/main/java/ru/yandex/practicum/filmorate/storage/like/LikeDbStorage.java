package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String ADD_LIKE_QUERY = """
            MERGE INTO likes (film_id, user_id) KEY(film_id, user_id) VALUES (?, ?);
            """;

    private static final String DELETE_LIKE_QUERY = """
            DELETE FROM likes WHERE film_id = ? AND user_id = ?
            """;

    private static final String GET_LIKES_QUERY = """
            SELECT user_id FROM likes WHERE film_id = ?
            """;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public Set<Long> getLikesByFilmId(Long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_LIKES_QUERY,
                (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }
}
