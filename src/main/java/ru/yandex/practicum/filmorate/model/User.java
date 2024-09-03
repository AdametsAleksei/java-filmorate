package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.Marker;
import ru.yandex.practicum.filmorate.validation.annotation.Login;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private @NotNull(groups = Marker.OnUpdate.class) Long id;
    private @Email String email;
    private @Login String login;
    private String name;
    private @Past LocalDate birthday;

    public void emptyName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}