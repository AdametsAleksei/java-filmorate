package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EventService eventService;

    @GetMapping("/{userID}")
    public User getById(@PathVariable("userID") long id) {
        log.info("Запрошен пользователь с ID - {}", id);
        return userService.get(id);
    }

    @GetMapping("{userID}/friends")
    public Collection<User> getFriends(@PathVariable Long userID) {
        log.info("Запрошен список всех друзей для пользователя с ID - {}", userID);
        return userService.getFriends(userID);
    }

    @GetMapping("{userID}/friends/common/{anotherUserID}")
    public Collection<User> getCommonFriends(@PathVariable Long userID, @PathVariable Long anotherUserID) {
        log.info("Запрошен список общих друзей для пользователей с ID - {}, {}", userID, anotherUserID);
        return userService.getCommonFriends(userID, anotherUserID);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Запрошен список всех пользователей");
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя: start");
        user = userService.create(user);
        log.info("Создан пользователь - {}", user);
        return user;
    }

    @PutMapping("{userID}/friends/{friendID}")
    public void addFriend(@PathVariable("userID") Long userID, @PathVariable Long friendID) {
        log.info("Добавление в друзья : start");
        userService.addFriend(userID, friendID);
        log.info("Пользователь с ID - {}, добавил в друзья пользователя с ID - {}", userID, friendID);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление пользователя: start");
        newUser = userService.update(newUser);
        log.info("Обновлен пользователь - {}", newUser);
        return newUser;
    }

    @DeleteMapping("{userID}/friends/{friendID}")
    public void deleteFriend(@PathVariable Long userID, @PathVariable Long friendID) {
        log.info("Удаление из друзей : start");
        userService.deleteFriend(userID, friendID);
        log.info("Пользователь с ID - {}, удалил из друзей пользователя с ID - {}", userID, friendID);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя по ID: {}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с ID: {} удален", userId);
    }

    @GetMapping("/{userId}/feed")
    public List<Event> getEvents(@PathVariable long userId) {
        log.info("Запрошены последние события пользователя с ID - {}", userId);
        return eventService.getEvents(userId);
    }

    @GetMapping("{userID}/recommendations")
    public List<Film> recommendations(@PathVariable Long userID) {
        log.info("Запрошен список рекомендаций");
        return userService.recommendations(userID);
    }
}