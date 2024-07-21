package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.Marker;
import ru.yandex.practicum.filmorate.validation.annotation.Login;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void whenEmailIsIncorrectlyShouldBeFalse() {
        User user = new User();
        user.setEmail("afsand.ru");
        user.setLogin("sad");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not email");
    }

    @Test
    void whenEmailIsCorrectlyShouldBeTrue() {
        User user = new User();
        user.setLogin("Asd");
        user.setEmail("afsand@yandex.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(),"Violation not found");
    }

    @Test
    void whenLoginIsEmptyShouldBeFalse() {
        User user = new User();
        user.setEmail("afsand@yandex.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Login.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not login");
    }

    @Test
    void whenLoginContainSpaceShouldBeFalse() {
        User user = new User();
        user.setEmail("afsand@yandex.ru");
        user.setLogin("as df");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Login.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not login");
    }

    @Test
    void whenLoginIsCorrectlyShouldBeTrue() {
        User user = new User();
        user.setEmail("afsand@yandex.ru");
        user.setLogin("Asd");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(),"Violation not found");
    }

    @Test
    void whenBirthdayDateInFutureShouldBeFalse() {
        User user = new User();
        user.setEmail("afsand@yandex.ru");
        user.setLogin("Asd");
        user.setBirthday(LocalDate.parse("2077-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Past.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not past");
    }

    @Test
    void whenUserUpdateShouldAskIDAndReturnFalse() {
        User user = new User();
        user.setEmail("afsand@yandex.ru");
        user.setLogin("Asd");
        user.setBirthday(LocalDate.parse("1977-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.OnUpdate.class);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not ID");
    }
}
