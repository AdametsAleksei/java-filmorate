package ru.yandex.practicum.filmorate.service.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.Event.EventRepository;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public void addEvent(Event event) {
        userRepository.isUserNotExists(event.getUserId());
        eventRepository.addEvent(event);
    }

    @Override
    public List<Event> getEvents(Long userId) {
        userRepository.isUserNotExists(userId);
        return eventRepository.getEvent(userId);
    }
}
