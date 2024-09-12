package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.Marker;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private @NotNull(groups = Marker.OnUpdate.class) Long eventId;
    private @NotNull Long userId;
    private @NotNull Long entityId;
    private Long timestamp;
    private @NotNull EventType eventType;
    private @NotNull Operation operation;

    public enum EventType {
        LIKE,
        REVIEW,
        FRIEND
    }

    public enum Operation {
        REMOVE,
        ADD,
        UPDATE
    }
}