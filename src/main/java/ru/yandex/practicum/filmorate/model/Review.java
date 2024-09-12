package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.Marker;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @NotNull(groups = Marker.OnUpdate.class)
    private Long reviewId;
    @NotNull
    @Size(max = 200)
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull(groups = Marker.OnCreate.class)
    private Long userId;
    @NotNull(groups = Marker.OnCreate.class)
    private Long filmId;
    private Long useful;
}