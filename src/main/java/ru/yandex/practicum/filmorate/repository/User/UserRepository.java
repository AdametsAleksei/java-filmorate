package ru.yandex.practicum.filmorate.repository.User;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAll();

    Optional<User> getById(Long id);

    User create(User user);

    User update(User newUser);

    void delete(Long id);

    void addToFriends(Long id, Long friendId);

    void deleteFromFriends(Long id, Long friendId);

    Collection<User> findAllFriends(Long id);

    Collection<User> findCommonFriends(Long id, Long otherId);

}
