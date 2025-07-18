package adamets.filmorate.controller;

import adamets.filmorate.model.User;
import adamets.filmorate.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class RestUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        log.info("==> Создание пользователя {}", user);
        User createdUser = userService.createUser(user);
        log.info("<== Пользователь создан {}", createdUser);
        return ResponseEntity.status(HttpStatus.OK).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
        log.info("==> Обновление пользователя {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("<== Пользователь обновлен {}", updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        log.info("==> Получение всех пользователей");
        Iterable<User> listUsers = userService.getAllUsers();
        log.info("<== Получение всех пользователей");
        return ResponseEntity.status(HttpStatus.OK).body(listUsers);
    }

    @GetMapping(path = "/{id:\\d+}")
    public ResponseEntity<User> getUserById(@PathVariable("id") @Min(1) Long userId) {
        log.info("==> Получение пользователя с ID - {}", userId);
        User user = userService.getUserById(userId);
        log.info("<== Получен пользователь с ID - {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping(path = "/{id:\\d+}/friends/{friendId:\\d+}")
    public ResponseEntity<Void> addFriend(@PathVariable("id") @Min(1) Long userId,
                                          @PathVariable("friendId") @Min(1) Long friendId) {
        log.info("==> Пользователь с ID - {}, добавляет в друзья пользователя с ID - {}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("<== Пользователь с ID - {}, добавил в друзья пользователя с ID - {}", userId, friendId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "{id:\\d+}/friends")
    public ResponseEntity<Iterable<User>> getAllUsersFriends(@PathVariable("id") Long userId) {
        log.info("==> Request list of friends for user with ID - {}", userId);
        Iterable<User> friendList = this.userService.getAllUsersFriends(userId);
        log.info("<== Retrieve list of friends for user with ID - {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(friendList);
    }

    @DeleteMapping(path = "{id:\\d+}/friends/{friendId:\\d+}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("id") @Min(1) Long userId,
                                             @PathVariable("friendId") @Min(1) Long friendId) {
        log.info("==> Delete friend with ID {}, by User {}", friendId, userId);
        userService.deleteFriend(userId, friendId);
        log.info("<== Success deleted friend with ID {}, by User {} ", friendId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "{id:\\d+}/friends/common/{otherId:\\d+}")
    public ResponseEntity<Iterable<User>> getUsersFriends(@PathVariable("id") Long userId,
                                                          @PathVariable("otherId") Long otherId) {
        log.info("==> Request list common friends for user with id - {}, and user with id {}", userId, otherId);
        Iterable<User> listFriends = userService.getAllCommonFriends(userId, otherId);
        log.info("<== Retrieve list common friends for user with id - {}, and user with id {}", userId, otherId);
        return ResponseEntity.status(HttpStatus.OK).body(listFriends);
    }
}
