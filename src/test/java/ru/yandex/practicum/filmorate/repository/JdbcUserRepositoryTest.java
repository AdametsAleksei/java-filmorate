package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.User.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;
import ru.yandex.practicum.filmorate.repository.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcUserRepository.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepositoryTest")
public class JdbcUserRepositoryTest {
    private final UserRepository userRepository;
    public static final Long TEST_USER_ID = 101L;

    public User getTestUser() {
        return User.builder()
                .id(TEST_USER_ID)
                .email("test@yandex.ru")
                .name("Test")
                .login("Test")
                .birthday(LocalDate.parse("1995-08-22"))
                .build();
    }

    @Test
    @DisplayName("Пользователь из БД должен быть таким же, как и user из метода")
    public void shouldReturnUserWhenFindById() {
        Optional<User> userOptional = userRepository.getById(TEST_USER_ID);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestUser());
    }

    @Test
    @DisplayName("Должен возвращаться список всех пользователей")
    public void shouldReturnListAllUsers() {
        assertEquals(3,userRepository.getAll().size());
    }

    @Test
    @DisplayName("Пользователь должен добавляться в БД")
    public void shouldBeCreatedNewUser() {
        User newUser = User.builder()
                .email("test@yandex.ru")
                .name("Test")
                .login("Test")
                .birthday(LocalDate.parse("1995-08-22"))
                .build();
        userRepository.create(newUser);
        assertThat(userRepository.getById(newUser.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    @DisplayName("Пользователь должен быть обновлен")
    public void shouldBeUpdatedUser() {
        User updateUser = getTestUser();
        updateUser.setName("UpdatedNameTest");
        updateUser.setEmail("updated@yandex.ru");
        userRepository.update(updateUser);
        assertThat(userRepository.getById(TEST_USER_ID))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(updateUser);
    }

    @Test
    @DisplayName("Пользователь должен быть удален")
    public void shouldBeDeletedUser() {
        int countFilm = userRepository.getAll().size();
        userRepository.delete(102L);
        assertEquals(countFilm - 1,userRepository.getAll().size());
    }
}