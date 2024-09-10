package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.Event.EventRepository;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final EventRepository eventRepository;
    private final FilmService filmService;


    @Override
    public void addFriend(Long userID, Long friendID) {
        users.isUserNotExists(userID);
        users.isUserNotExists(friendID);
        users.addToFriends(userID, friendID);
        Event event = Event.builder()
                .userId(userID)
                .entityId(friendID)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(Event.EventType.FRIEND)
                .operation(Event.Operation.ADD)
                .build();
        eventRepository.addEvent(event);
        log.info("Пользователь с id = {} и пользователь с id = {} теперь друзья", userID, friendID);
    }

    @Override
    public void deleteFriend(Long userID, Long friendID) {
        users.isUserNotExists(userID);
        users.isUserNotExists(friendID);
        users.deleteFromFriends(userID, friendID);
        Event event = Event.builder()
                .userId(userID)
                .entityId(friendID)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(Event.EventType.FRIEND)
                .operation(Event.Operation.REMOVE)
                .build();
        eventRepository.addEvent(event);
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

    @Override
    public List<Film> recommendations(Long userId) {
        users.isUserNotExists(userId);
        return filmService.recommendations(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Проверка пользователя с id = {}", userId);
        users.isUserNotExists(userId);
        users.deleteUser(userId);
    }
}
