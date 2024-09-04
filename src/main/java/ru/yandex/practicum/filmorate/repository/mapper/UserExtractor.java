package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserExtractor implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        User user = null;
        while (rs.next()) {
            if (user == null) {
                user = new User();
            }
            user.setId(rs.getLong("USER_ID"));
            user.setName(rs.getString("NAME"));
            user.setLogin(rs.getString("LOGIN"));
            user.setEmail(rs.getString("EMAIL"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        }
        return user;
    }
}
