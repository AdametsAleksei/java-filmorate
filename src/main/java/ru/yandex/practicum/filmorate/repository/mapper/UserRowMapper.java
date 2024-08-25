package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .name(rs.getString("NAME"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
