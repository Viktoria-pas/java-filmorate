package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.List;

@Component
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipRowMapper friendshipRowMapper;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, FriendshipRowMapper friendshipRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipRowMapper = friendshipRowMapper;
    }
    private static final String CHECK_REQUEST_QUERY =
            "SELECT status_id FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String UPDATE_STATUS_QUERY =
            "UPDATE friendships SET status_id = ? WHERE (user_id = ? AND friend_id = ?)";
    private static final String CHECK_EXIST_REQUEST_QUERY =
            "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_REQUEST_QUERY =
            "INSERT INTO friendships (user_id, friend_id, status_id) VALUES (?, ?, ?)";
    private static final String DELETE_FROM_FRIENDS_QUERY =
            "DELETE FROM friendships WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    private static final String FIEND_FRIENDS_QUERY =
            "SELECT * FROM friendships WHERE user_id = ?";


    public void addFriendship(Long userId, Long friendId) {
        if (jdbcTemplate.queryForObject(CHECK_EXIST_REQUEST_QUERY, Integer.class, userId, friendId) > 0) {
            throw new IllegalStateException("Friendship request already exists");
        }

        jdbcTemplate.update(INSERT_REQUEST_QUERY,
                userId,
                friendId,
                FriendshipStatus.CONFIRMED.getId());
    }

    @Override
    public void removeFriendship(Long userId, Long friendId) {
        Integer statusId;
        try {
            statusId = jdbcTemplate.queryForObject(CHECK_REQUEST_QUERY, Integer.class, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            return;
        }
        if (statusId.equals(FriendshipStatus.CONFIRMED.getId())) {
            jdbcTemplate.update(UPDATE_STATUS_QUERY, FriendshipStatus.PENDING.getId(), friendId, userId);
        }
        jdbcTemplate.update(DELETE_FROM_FRIENDS_QUERY, userId, friendId, friendId, userId);
    }

    @Override
    public List<Friendship> findFriendshipsByUserId(Long userId) {
        return jdbcTemplate.query(FIEND_FRIENDS_QUERY
                , friendshipRowMapper, userId);
    }

    @Override
    public void updateFriendship(Friendship friendship) {
        jdbcTemplate.update(UPDATE_STATUS_QUERY,
                friendship.getStatus().getId(), friendship.getUserId(), friendship.getFriendId());
    }
}
