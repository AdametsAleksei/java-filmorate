package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserService {
    void addFriend(Long userID, Long friendID);

    void deleteFriend(Long userID, Long friendID);

    Set<User> getCommonFriends(Long userID, Long anotherUserID);

    Set<User> getFriends(Long userID);

    User get(Long id);

    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    Long getNextId();
}
