package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public void delete(Long userID) {
        users.delete(userID);
    }

    @Override
    public void addFriend(Long userID, Long friendID) {
        users.addToFriends(userID, friendID);
        log.info("Добавлен новый друг с id={}", friendID);
    }

    @Override
    public void deleteFriend(Long userID, Long friendID) {
        users.deleteFromFriends(userID, friendID);
    }

    @Override
    public Collection<User> getFriends(Long userID) {
        return users.findAllFriends(userID);
    }

    @Override
    public Collection<User> getCommonFriends(Long userID, Long anotherUserID) {
        return users.findCommonFriends(userID, anotherUserID);
    }

    @Override
    public User get(Long userID) {
        return users.getById(userID).orElseThrow(() -> new NotFoundException(
               "Пользователь c ID - " + userID + " не найден"));
    }

    @Override
    public Collection<User> findAll() {
        return users.getAll();
    }

    @Override
    public User create(User user) {
        return users.create(user);
    }

    @Override
    public User update(User newUser) {
        return users.update(newUser);
    }

}
