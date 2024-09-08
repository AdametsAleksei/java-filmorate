package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.Marker;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private @NotNull(groups = Marker.OnUpdate.class) Long id;
    private @NotNull String name;
}
