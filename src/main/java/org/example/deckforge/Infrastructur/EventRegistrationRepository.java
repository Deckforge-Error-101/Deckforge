package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.EventRegistration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRegistrationRepository implements IEventRegistrationRepository {
    private final JdbcTemplate jdbcTemplate;

    public EventRegistrationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createRegistration(EventRegistration Registration) {

    }

    @Override
    public EventRegistration findById(int registrationId) {
        return null;
    }

    @Override
    public List<EventRegistration> findAllRegistrations() {
        return List.of();
    }
}
