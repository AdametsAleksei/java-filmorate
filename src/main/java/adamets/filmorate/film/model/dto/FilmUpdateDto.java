package adamets.filmorate.film.model.dto;

import adamets.filmorate.exceptions.customValidAnnotation.MinDate;
import adamets.filmorate.genre.Genre;
import adamets.filmorate.mpa.Mpa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record FilmUpdateDto(
        @NotNull
        Long id,
        String name,
        @Size(max = 200, message = "Не должно превышать 200 знаков")
        String description,
        @MinDate
        LocalDate releaseDate,
        @Positive
        Integer duration,
        Mpa mpa,
        Set<Genre> genres) {
}
