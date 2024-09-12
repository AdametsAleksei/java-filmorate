package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventsExtractor implements ResultSetExtractor<List<Event>> {

    @Override
    public List<Event> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event event = new Event();
            event.setEventId(rs.getLong("EVENT_ID"));
            event.setUserId(rs.getLong("USER_ID"));
            event.setEntityId(rs.getLong("ENTITY_ID"));
            event.setTimestamp(rs.getLong("TIMESTAMP"));
            event.setEventType(Event.EventType.valueOf(rs.getString("EVENT_TYPE")));
            event.setOperation(Event.Operation.valueOf(rs.getString("OPERATION")));
            events.add(event);
        }
        return events;
    }
}