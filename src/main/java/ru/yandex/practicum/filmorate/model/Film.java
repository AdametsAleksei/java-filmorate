package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.Marker;
import ru.yandex.practicum.filmorate.validation.annotation.YearOfRelease;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private @NotNull(groups = Marker.OnUpdate.class) Long id;
    private @NotBlank String name;
    private @Size(max = 200) String description;
    private @YearOfRelease LocalDate releaseDate;
    private @Positive Integer duration;
    private @NotNull Mpa mpa;
    private @Builder.Default LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private LinkedHashSet<Director> directors = new LinkedHashSet<>();
}