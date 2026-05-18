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
    public void createRegistration(EventRegistration registration) {
        String sql = "INSERT INTO EventRegistrations (eventId, userId, registrationDate) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                registration.getEventId(),
                registration.getUserId(),
                registration.getRegistrationDate()
        );
    }

    @Override
    public void deleteRegistration(int registrationId) {
        String sql = "DELETE FROM EventRegistrations WHERE registrationId = ?";
        jdbcTemplate.update(sql, registrationId);
    }


    @Override
    public EventRegistration findById(int registrationId) {
        String sql = "SELECT * FROM EventRegistrations WHERE registrationId = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            EventRegistration registration = new EventRegistration();
            try {
                registration.setRegistrationId(rs.getInt("registrationId"));
                registration.setEventId(rs.getInt("eventId"));
                registration.setUserId(rs.getInt("userId"));
                int deckId = rs.getInt("deckId");
                if (!rs.wasNull()) {
                    registration.setDeckId(deckId);
                }
                registration.setRegistrationDate(rs.getTimestamp("registrationDate").toLocalDateTime());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return registration;
        }, registrationId);
    }

    @Override
    public List<EventRegistration> findAllRegistrations() {
        String sql = "SELECT * FROM EventRegistrations";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EventRegistration registration = new EventRegistration();
            try {
                registration.setRegistrationId(rs.getInt("registrationId"));
                registration.setEventId(rs.getInt("eventId"));
                registration.setUserId(rs.getInt("userId"));
                int deckId = rs.getInt("deckId");
                if (!rs.wasNull()) {
                    registration.setDeckId(deckId);
                }
                registration.setRegistrationDate(rs.getTimestamp("registrationDate").toLocalDateTime());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return registration;
        });
    }

    @Override
    public List<EventRegistration> findAllByUserId(int userId) {
        String sql = "SELECT * FROM EventRegistrations WHERE userId = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EventRegistration registration = new EventRegistration();
            try {
                registration.setRegistrationId(rs.getInt("registrationId"));
                registration.setEventId(rs.getInt("eventId"));
                registration.setUserId(rs.getInt("userId"));
                int deckId = rs.getInt("deckId");
                if (!rs.wasNull()) {
                    registration.setDeckId(deckId);
                }
                registration.setRegistrationDate(rs.getTimestamp("registrationDate").toLocalDateTime());
            } catch (Exception e) {
                throw new RuntimeException("Kritisk fejl");
            }
            return registration;
        }, userId);
    }

    @Override
    public boolean existsByEventIdAndUserId(int eventId, int userId) {
        return false;
    }

    @Override
    public void addDeckToRegistration(int registrationId, int deckId) {
        String sql = """
            UPDATE EventRegistrations
            SET deckId = ?
            WHERE registrationId = ?
            """;

        jdbcTemplate.update(sql, deckId, registrationId);

    }
}