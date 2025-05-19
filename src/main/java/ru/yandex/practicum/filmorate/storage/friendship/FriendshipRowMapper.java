package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setUserId(rs.getLong("user_id"));
        friendship.setFriendId(rs.getLong("friend_id"));
        friendship.setStatus(FriendshipStatus.fromId(rs.getInt("status_id")));
        return friendship;
    }

}
