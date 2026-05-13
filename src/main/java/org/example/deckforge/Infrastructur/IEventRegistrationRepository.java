package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.EventRegistration;

import java.util.List;

public interface IEventRegistrationRepository {
    void createRegistration(EventRegistration registration);
    void deleteRegistration(int registrationId);
    EventRegistration findById(int registrationId);
    List<EventRegistration> findAllRegistrations();
    List<EventRegistration> findAllByUserId(int userId);
    boolean existsByEventIdAndUserId(int eventId, int userId);
    void addDeckToRegistration(int registrationId, int deckId);

}
