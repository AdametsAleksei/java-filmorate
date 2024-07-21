package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validation.annotation.YearOfRelease;

import java.time.LocalDate;

public class YearOfReleaseValidator implements ConstraintValidator<YearOfRelease, LocalDate> {
    private String date;

    public void initialize(YearOfRelease parameters) {
        this.date = parameters.date();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && value.isAfter(LocalDate.parse(date));
    }
}
