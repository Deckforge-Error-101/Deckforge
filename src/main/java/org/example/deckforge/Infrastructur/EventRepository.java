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
        String sql = "INSERT INTO Events (title, eventType, capacity) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                event.getTitle(),
                event.getEventType(),
                event.getCapacity()
        );
    }

    @Override
    public void deleteEvent(int eventId) {
        String sql = "DELETE FROM Events WHERE eventId = ?";
        jdbcTemplate.update(sql, eventId);
    }

    @Override
    public void updateEvent(int eventId) {

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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return event;
        });
    }

    @Override
    public Event findById(int eventId) {
        String sql = "SELECT * FROM Events WHERE eventId = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Event event = new Event();
            event.setEventId(rs.getInt("eventId"));
            try{
            event.setTitle(rs.getString("title"));
            event.setEventType(rs.getString("eventType"));
            event.setCapacity(rs.getInt("capacity"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return event;
        }, eventId);
    }
}
