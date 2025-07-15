package adamets.filmorate.user.controller;

import adamets.filmorate.user.model.User;
import adamets.filmorate.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class RestUserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("==> Создание пользователя {}", user);
        User createdUser = userService.createUser(user);
        log.info("<== Пользователь создан {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("==> Обновление пользователя {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("<== Пользователь обновлен {}", updatedUser);
        return updatedUser;
    }

    @GetMapping
    public Iterable<User> getAllUsers() {
        log.info("==> Получение всех пользователей");
        Iterable<User> listUsers = userService.getAllUsers();
        log.info("<== Получение всех пользователей");
        return listUsers;
    }
}
