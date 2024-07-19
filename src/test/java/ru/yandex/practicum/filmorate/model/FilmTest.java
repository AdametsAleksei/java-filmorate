package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.Marker;
import ru.yandex.practicum.filmorate.validation.annotation.YearOfRelease;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void whenFilmIsCorrectShouldBeTrue() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDuration(1);
        film.setReleaseDate(LocalDate.parse("1926-01-12"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(),"Violation not found");
    }

    @Test
    void whenFilmUpdateShouldAskIDAndReturnFalse() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDuration(-11);
        film.setReleaseDate(LocalDate.parse("1926-01-12"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.OnUpdate.class);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not ID");
    }

    @Test
    void whenNameIsEmptyShouldBeFalse() {
        Film film = new Film();
        film.setDuration(1);
        film.setReleaseDate(LocalDate.parse("1926-01-12"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not email");
    }

    @Test
    void whenDescriptionContain200CharShouldBeFalse() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDuration(1);
        film.setReleaseDate(LocalDate.parse("1926-01-12"));
        String description = "B".repeat(201);
        film.setDescription(description);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Size.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not size");
    }

    @Test
    void whenReleaseYearIsBeforeMinDateShouldBeFalse() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDuration(1);
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(YearOfRelease.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not year");
    }

    @Test
    void whenDurationIsNegativeShouldBeFalse() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDuration(-11);
        film.setReleaseDate(LocalDate.parse("1926-01-12"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Positive.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "Not year");
    }
}
