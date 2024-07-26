package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private Long id = 0L;
    private final Map<Long, Set<User>> userFriendIds = new HashMap<>();

    @Override
    public void addFriend(Long userID, Long friendID) {
        get(userID);
        get(friendID);
        Set<User> uFriendsIDs = userFriendIds.computeIfAbsent(userID, id -> new HashSet<>());
        uFriendsIDs.add(get(friendID));
        Set<User> fFriendsIDs = userFriendIds.computeIfAbsent(friendID, id -> new HashSet<>());
        fFriendsIDs.add(get(userID));
    }

    @Override
    public void deleteFriend(Long userID, Long friendID) {
        get(userID);
        get(friendID);
        Set<User> uFriendsIDs = userFriendIds.computeIfAbsent(userID, id -> new HashSet<>());
        if (!uFriendsIDs.isEmpty()) {
            if (uFriendsIDs.contains(get(friendID))) {
                uFriendsIDs.remove(get(friendID));
            } else {
                throw new NotFoundException("404 user");
            }
        } else {
            return;
        }
        Set<User> fFriendsIDs = userFriendIds.computeIfAbsent(friendID, id -> new HashSet<>());
        if (!fFriendsIDs.isEmpty()) {
            if (fFriendsIDs.contains(get(userID))) {
                fFriendsIDs.remove(get(userID));
            } else {
                throw new NotFoundException("404 friend");
            }
        }
    }

    @Override
    public Set<User> getFriends(Long userID) {
        get(userID);
        return new HashSet<>(userFriendIds.computeIfAbsent(userID, id -> new HashSet<>()));
    }

    @Override
    public Set<User> getCommonFriends(Long userID, Long anotherUserID) {
        Set<User> commonFriends = new HashSet<>(getFriends(userID));
        commonFriends.retainAll(getFriends(anotherUserID));
        return commonFriends;
    }

    @Override
    public User get(Long userID) {
        return users.get(userID).orElseThrow(() -> new NotFoundException(
               "Пользователь c ID - " + userID + " не найден"));
    }

    @Override
    public Collection<User> findAll() {
        return users.getAll();
    }

    @Override
    public User create(User user) {
        user.emptyName();
        user.setId(getNextId());
        users.add(user);
        return user;
    }

    @Override
    public User update(User newUser) {
        get(newUser.getId());
        newUser.emptyName();
        users.add(newUser);
        return newUser;
    }

    @Override
    public Long getNextId() {
        return ++id;
    }

}
