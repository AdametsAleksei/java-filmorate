package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    void add(User user);

    void delete(User user);

    Optional<User> get(Long id);

    Collection<User> getAll();

}
