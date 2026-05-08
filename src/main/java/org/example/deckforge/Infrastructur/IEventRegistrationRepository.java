package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.EventRegistration;

import java.util.List;

public interface IEventRegistrationRepository {
    void createRegistration(EventRegistration Registration);
    EventRegistration findById(int registrationId);
    List<EventRegistration> findAllRegistrations();
}
