package ru.yandex.practicum.filmorate.repository.User;


import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.UsersExtractor;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final UserExtractor userExtractor;
    private final UsersExtractor usersExtractor;

    @Override
    public Collection<User> getAll() {
        String sql = """
                     SELECT *
                     FROM USERS;
                     """;
        return jdbc.query(sql, usersExtractor);
    }

    @Override
    public Optional<User> getById(Long id) {
        String sql = """
                     SELECT *
                     FROM USERS
                     WHERE USER_ID = :user_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("user_id", id);
        System.out.println(jdbc.query(sql, parameter, userExtractor));
        return Optional.ofNullable(jdbc.query(sql, parameter, userExtractor));
    }

    @Override
    public User create(User user) {
        user.emptyName();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                     INSERT INTO USERS ("EMAIL", "LOGIN", "NAME", "BIRTHDAY")
                     VALUES (:email, :login, :name, :birthday);
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());
        jdbc.update(sql, parameter, keyHolder);
        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public User update(User user) {
        String sql = """
                     UPDATE USERS
                     SET "EMAIL" = :email,
                         "LOGIN" = :login,
                         "NAME" = :name,
                         "BIRTHDAY" = :birthday
                     WHERE USER_ID = :user_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());
        jdbc.update(sql, parameter);
        return user;
    }

    @Override
    public void addToFriends(Long id, Long friendId) {
        String sql = """
                     INSERT INTO FRIENDS (USER_ID, FRIEND_ID)
                     VALUES (:user_id, :friend_id);
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("friend_id", friendId);
        jdbc.update(sql, parameter);
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        String sql = """
                     DELETE FROM FRIENDS
                     WHERE USER_ID = :user_id
                     AND FRIEND_ID = :friend_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("friend_id", friendId);
        jdbc.update(sql, parameter);
    }

    @Override
    public Collection<User> findAllFriends(Long id) {
        String sql = """
                     SELECT *
                     FROM USERS AS u
                     WHERE USER_ID IN (
                        SELECT FRIEND_ID
                        FROM FRIENDS
                        WHERE USER_ID = :user_id
                        );
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id);
        return jdbc.query(sql, parameter, usersExtractor);
    }

    @Override
    public Collection<User> findCommonFriends(Long id, Long otherId) {
        String sql = """
                     SELECT *
                     FROM USERS
                     WHERE USER_ID IN (
                        SELECT f.friend_id
                        FROM USERS AS u
                        LEFT JOIN FRIENDS AS f ON u.user_id = f.user_id
                        WHERE u.user_id = :user_id AND f.friend_id IN (
                            SELECT fr.friend_id
                            FROM USERS AS us
                            LEFT JOIN FRIENDS AS fr ON us.user_id = fr.user_id
                            WHERE us.user_id = :otherId
                        )
                     );
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("otherId", otherId);
        return jdbc.query(sql, parameter, usersExtractor);
    }

    @Override
    public void isUserNotExists(Long id) {
        if (getById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
