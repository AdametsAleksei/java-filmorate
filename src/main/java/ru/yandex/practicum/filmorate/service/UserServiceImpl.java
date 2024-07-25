package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserStorage users;
    private Long id = 0L;

    public Collection<User> findAll() {
        return users.getAll();
    }

    public User create(User user) {
        user.emptyName();
        user.setId(getNextId());
        users.add(user);
        return user;
    }

    public User update(User newUser) {
        Optional<User> newUserOptional = users.get(newUser.getId());
        if (newUserOptional.isPresent()) {
            newUser.emptyName();
            users.add(newUser);
            return newUser;
        } else {
            throw new NotFoundException("Пользователь с указанным id не найден");
        }

    }

    private long getNextId() {
        return ++id;
    }
}
