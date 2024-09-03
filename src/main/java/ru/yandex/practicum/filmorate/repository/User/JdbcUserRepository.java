package ru.yandex.practicum.filmorate.repository.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcUserRepository extends BaseDbRepository<User> implements UserRepository {

    private static final String USERS_FIND_ALL_QUERY = """
            SELECT *
            FROM USERS;
            """;
    private static final String USERS_INSERT_QUERY = """
            INSERT INTO USERS ("EMAIL", "LOGIN", "NAME", "BIRTHDAY")
                        VALUES (?, ?, ?, ?);
            """;
    private static final String USERS_UPDATE_QUERY = """
            UPDATE USERS
            SET "EMAIL" = ?,
                "LOGIN" = ?,
                "NAME" = ?,
                "BIRTHDAY" = ?
            WHERE USER_ID = ?;
            """;
    private static final String USERS_ADD_TO_FRIENDS_QUERY = """
            INSERT INTO FRIENDS (USER_ID, FRIEND_ID)
            VALUES (?, ?);
            """;
    private static final String USERS_DELETE_FROM_FRIENDS_QUERY = """
            DELETE FROM FRIENDS
            WHERE USER_ID = ?
                AND FRIEND_ID = ?;
            """;
    private static final String USERS_FIND_ALL_FRIENDS_QUERY = """
            SELECT *
            FROM USERS AS u
            WHERE USER_ID IN (
                SELECT FRIEND_ID
                FROM FRIENDS
                WHERE USER_ID = ?
                );
            """;
    private static final String USERS_FIND_COMMON_FRIENDS_QUERY = """
            SELECT *
            FROM USERS AS u
            WHERE u.USER_ID IN (
                SELECT friends_of_first.friend
                FROM (
                    SELECT FRIEND_ID AS friend FROM FRIENDS WHERE USER_ID = ?
                    UNION
                    SELECT USER_ID AS friend FROM FRIENDS WHERE FRIEND_ID = ?
                    ) AS friends_of_first
                JOIN (
                    SELECT FRIEND_ID AS friend FROM FRIENDS WHERE USER_ID = ?
                    UNION
                    SELECT USER_ID AS friend FROM FRIENDS WHERE FRIEND_ID = ?
                    ) AS friends_of_second
                ON friends_of_first.friend = friends_of_second.friend
            );
            """;
    private static final String USERS_FIND_BY_ID_QUERY = """
            SELECT *
            FROM USERS
            WHERE USER_ID = ?;
            """;
    private static final String USERS_DELETE = """
            DELETE FROM USERS
            WHERE USER_ID = ?;
            """;

    public JdbcUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAll() {
        log.info("Получение списка пользователей");
        return findMany(USERS_FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getById(Long id) {
        List<User> users = findMany(
                USERS_FIND_BY_ID_QUERY,
                id
        );
        return Optional.ofNullable(users.getFirst());
    }

    @Override
    public User create(User user) {
        user.emptyName();
        long id = insertGetKey(
                USERS_INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                java.sql.Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        log.info("Пользователь {} добавлен в список с id = {}", user.getName(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (isUserNotExists(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        update(
                USERS_UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                java.sql.Date.valueOf(user.getBirthday()),
                user.getId()
        );
        log.info("Пользователь с id = {} обновлен", user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        if (isUserNotExists(id))
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        delete(USERS_DELETE, id);
        log.info("Пользователь с id = {} удален", id);
    }

    @Override
    public void addToFriends(Long id, Long friendId) {
        if (isUserNotExists(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        if (isUserNotExists(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        insert(USERS_ADD_TO_FRIENDS_QUERY, id, friendId);
        log.info("Пользователь с id = {} и пользователь с id = {} теперь друзья", friendId, id);
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        if (isUserNotExists(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        if (isUserNotExists(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        delete(USERS_DELETE_FROM_FRIENDS_QUERY, id, friendId);
        log.info("Пользователь с id = {} и пользователь с id = {} больше не друзья", friendId, id);
    }

    @Override
    public Collection<User> findAllFriends(Long id) {
        if (isUserNotExists(id))
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        log.info("Поиск друзей пользователя с id = {}", id);
        return findMany(USERS_FIND_ALL_FRIENDS_QUERY, id);
    }

    @Override
    public Collection<User> findCommonFriends(Long id, Long otherId) {
        if (isUserNotExists(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        if (isUserNotExists(otherId)) {
            throw new NotFoundException("Пользователь с id = " + otherId + " не найден");
        }
        log.info("Поиск общих друзей пользователя с id = {} и пользователя с id = {}", id, otherId);
        return findMany(USERS_FIND_COMMON_FRIENDS_QUERY, id, id, otherId, otherId);
    }

    private boolean isUserNotExists(Long id) {
        return findOne(USERS_FIND_BY_ID_QUERY, id).isEmpty();
    }
}
