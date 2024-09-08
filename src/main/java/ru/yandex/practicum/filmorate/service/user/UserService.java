package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    void addFriend(Long userID, Long friendID);

    void deleteFriend(Long userID, Long friendID);

    Collection<User> getCommonFriends(Long userID, Long anotherUserID);

    Collection<User> getFriends(Long userID);

    User get(Long id);

    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    void deleteUser(Long userId);
}
