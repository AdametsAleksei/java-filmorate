package ru.yandex.practicum.filmorate.repository.Event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventRepository {

    void addEvent(Event event);

    List<Event> getEvent(Long userId);
}
