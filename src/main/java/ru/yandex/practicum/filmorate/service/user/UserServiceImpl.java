package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository users;

    @Override
    public void addFriend(Long userID, Long friendID) {
        users.isUserNotExists(userID);
        users.isUserNotExists(friendID);
        users.addToFriends(userID, friendID);
        log.info("Пользователь с id = {} и пользователь с id = {} теперь друзья", userID, friendID);
    }

    @Override
    public void deleteFriend(Long userID, Long friendID) {
        users.isUserNotExists(userID);
        users.isUserNotExists(friendID);
        users.deleteFromFriends(userID, friendID);
        log.info("Пользователь с id = {} и пользователь с id = {} больше не друзья", userID, friendID);
    }

    @Override
    public Collection<User> getFriends(Long userID) {
        log.info("Поиск друзей пользователя с id = {}", userID);
        users.isUserNotExists(userID);
        return users.findAllFriends(userID);
    }

    @Override
    public Collection<User> getCommonFriends(Long userID, Long anotherUserID) {
        log.info("Поиск общих друзей пользователя с id = {} и пользователя с id = {}", userID, anotherUserID);
        users.isUserNotExists(userID);
        users.isUserNotExists(anotherUserID);
        return users.findCommonFriends(userID, anotherUserID);
    }

    @Override
    public User get(Long userID) {
        log.info("Получение пользователя с id={}", userID);
        return users.getById(userID).orElseThrow(() -> new NotFoundException(
               "Пользователь c ID - " + userID + " не найден"));
    }

    @Override
    public Collection<User> findAll() {
        log.info("Получение списка пользователей");
        return users.getAll();
    }

    @Override
    public User create(User user) {
        users.create(user);
        if (user.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        log.info("Пользователь {} создан с id = {}", user.getName(), user.getId());
        return user;
    }

    @Override
    public User update(User newUser) {
        users.isUserNotExists(newUser.getId());
        users.update(newUser);
        log.info("Пользователь с id = {} обновлен", newUser.getId());
        return newUser;
    }
}
