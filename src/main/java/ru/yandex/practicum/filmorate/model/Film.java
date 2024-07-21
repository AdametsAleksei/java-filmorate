package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.Marker;
import ru.yandex.practicum.filmorate.validation.annotation.YearOfRelease;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Film {
    private @NotNull(groups = Marker.OnUpdate.class) Long id;
    private @NotBlank String name;
    private @Size(max = 200) String description;
    private @YearOfRelease(date = "1895-12-27") LocalDate releaseDate;
    private @Positive int duration;
}