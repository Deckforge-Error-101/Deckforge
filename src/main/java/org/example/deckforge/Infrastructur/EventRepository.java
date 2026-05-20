package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepository implements IEventRepository {
    private final JdbcTemplate jdbcTemplate;

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createEvent(Event event) {
        String sql = "INSERT INTO Events (title, eventType, capacity, statusType) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                event.getTitle(),
                event.getEventType(),
                event.getCapacity(),
                "OPEN"
        );
    }

    @Override
    public void deleteEvent(Event event) {
        String sql = "DELETE FROM Events WHERE eventId = ?";
        jdbcTemplate.update(sql, event.getEventId());
    }

    @Override
    public void updateEvent(Event event) {

        String sql = """
                UPDATE Events
                SET title = ?,
                    eventType = ?,
                    capacity = ?
                WHERE eventId = ?
                """;

        jdbcTemplate.update(
                sql,
                event.getTitle(),
                event.getEventType(),
                event.getCapacity(),
                event.getEventId()
        );
    }

    @Override
    public void updateStatus(Event event) {
        String sql = """
            UPDATE Events
            SET statusType = ?
            WHERE eventId = ?
            """;

        jdbcTemplate.update(sql,
                event.getStatusType(),
                event.getEventId()
        );
    }

    @Override
    public List<Event> findAllEvents() {
        String sql = "SELECT * FROM Events";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Event event = new Event();
            event.setEventId(rs.getInt("eventId"));
            try {
                event.setTitle(rs.getString("title"));
                event.setEventType(rs.getString("eventType"));
                event.setCapacity(rs.getInt("capacity"));
                event.setStatusType(rs.getString("statusType"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return event;
        });
    }

    @Override
    public Event findByEvent(Event event) {
        String sql = "SELECT * FROM Events WHERE eventId = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Event dbevent = new Event();
            dbevent.setEventId(rs.getInt("eventId"));
            try{
            dbevent.setTitle(rs.getString("title"));
            dbevent.setEventType(rs.getString("eventType"));
            dbevent.setCapacity(rs.getInt("capacity"));
            dbevent.setStatusType(rs.getString("statusType"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return dbevent;
        }, event.getEventId());
    }
}
