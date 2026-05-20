package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.User;
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
    public void deleteRegistration(EventRegistration registration) {
        String sql = "DELETE FROM EventRegistrations WHERE registrationId = ?";

        jdbcTemplate.update(
                sql,
                registration.getRegistrationId()
        );
    }


    @Override
    public EventRegistration findByRegistration(EventRegistration registration) {
        String sql = "SELECT * FROM EventRegistrations WHERE registrationId = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            EventRegistration dbRegistration = new EventRegistration();

            try {
                dbRegistration.setRegistrationId(rs.getInt("registrationId"));
                dbRegistration.setEventId(rs.getInt("eventId"));
                dbRegistration.setUserId(rs.getInt("userId"));

                int deckId = rs.getInt("deckId");
                if (!rs.wasNull()) {
                    dbRegistration.setDeckId(deckId);
                }

                dbRegistration.setRegistrationDate(
                        rs.getTimestamp("registrationDate").toLocalDateTime()
                );

            } catch (Exception e) {
                throw new RuntimeException("Kritisk fejl");
            }

            return dbRegistration;
        }, registration.getRegistrationId());
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
    public List<EventRegistration> findAllByUser(User user) {
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

                registration.setRegistrationDate(
                        rs.getTimestamp("registrationDate").toLocalDateTime()
                );

            } catch (Exception e) {
                throw new RuntimeException("Kritisk fejl");
            }

            return registration;
        }, user.getUserId());
    }

    @Override
    public boolean existsByEventAndUser(Event event, User user) {
        String sql = """
            SELECT COUNT(*)
            FROM EventRegistrations
            WHERE eventId = ? AND userId = ?
            """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                event.getEventId(),
                user.getUserId()
        );

        return count != null && count > 0;
    }

    @Override
    public void addDeckToRegistration(EventRegistration registration) {
        String sql = """
            UPDATE EventRegistrations
            SET deckId = ?
            WHERE registrationId = ?
            """;

        jdbcTemplate.update(
                sql,
                registration.getDeckId(),
                registration.getRegistrationId()
        );
    }

    @Override
    public int countRegistrationsByEvent(Event event) {
        String sql = """
            SELECT COUNT(*)
            FROM EventRegistrations
            WHERE eventId = ?
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                event.getEventId()
        );
        return 0;
    }


}