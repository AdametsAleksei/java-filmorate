package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Friendship.builder()
                .userId(rs.getLong("USER_ID"))
                .friendId(rs.getLong("FRIEND_ID"))
                .build();
    }
}
