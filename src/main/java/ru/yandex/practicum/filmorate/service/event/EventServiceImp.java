package ru.yandex.practicum.filmorate.service.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.Event.EventRepository;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImp implements EventService {
    EventRepository eventRepository;
    UserRepository userRepository;

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
