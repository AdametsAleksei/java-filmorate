package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("USER_ID"));
            user.setName(rs.getString("NAME"));
            user.setLogin(rs.getString("LOGIN"));
            user.setEmail(rs.getString("EMAIL"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
            users.add(user);
        }
        return users;
    }
}
