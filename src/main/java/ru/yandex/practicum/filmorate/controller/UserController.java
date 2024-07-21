package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрошен список всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя: start");
        user.emptyName();
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь - {}", user);
        return user;
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление пользователя: start");
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с id - {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с указанным id не найден");
        }
        newUser.emptyName();
        users.put(newUser.getId(), newUser);
        log.info("Обновлен пользователь - {}", newUser);
        return newUser;
    }

    private long getNextId() {
        return ++id;
    }
}
