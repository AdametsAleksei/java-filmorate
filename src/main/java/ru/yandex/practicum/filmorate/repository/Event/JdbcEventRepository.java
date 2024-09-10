package ru.yandex.practicum.filmorate.repository.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.mapper.EventsExtractor;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcEventRepository implements EventRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final EventsExtractor extractor;

    @Override
    public void addEvent(Event event) {
        String sql = """
                INSERT INTO USER_EVENTS (USER_ID, ENTITY_ID, TIMESTAMP, EVENT_TYPE, OPERATION)
                VALUES (:user_id,:entity_id,:timestamp,:event_type,:operation)
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", event.getUserId())
                .addValue("entity_id", event.getEntityId())
                .addValue("timestamp", event.getTimestamp())
                .addValue("event_type", event.getEventType().toString())
                .addValue("operation", event.getOperation().toString());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameter, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        event.setEventId(id);
    }

    @Override
    public List<Event> getEvent(Long userId) {
        String sql = """
                SELECT *
                FROM USER_EVENTS
                WHERE USER_ID = :user_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("user_id", userId);
        return jdbc.query(sql, parameter, extractor);
    }
}
