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

    }

    @Override
    public void deleteEvent(int eventId) {

    }

    @Override
    public void updateEvent(int eventId) {

    }

    @Override
    public List<Event> findAllEvents() {
        return List.of();
    }

    @Override
    public Event findById(int eventId) {
        return null;
    }
}
