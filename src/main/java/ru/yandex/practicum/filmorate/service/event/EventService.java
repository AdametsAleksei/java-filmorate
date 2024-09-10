package ru.yandex.practicum.filmorate.service.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventService {

    void addEvent(Event event);

    List<Event> getEvents(Long userId);
}

