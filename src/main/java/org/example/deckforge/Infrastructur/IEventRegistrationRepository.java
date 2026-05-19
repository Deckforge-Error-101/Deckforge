package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Domain.Event;

import java.util.List;

public interface IEventRegistrationRepository {
    void createRegistration(EventRegistration registration);
    void deleteRegistration(EventRegistration registration);
    EventRegistration findByRegistration(EventRegistration registration);
    List<EventRegistration> findAllRegistrations();
    List<EventRegistration> findAllByUser(User user);
    boolean existsByEventAndUser(Event event,User user);
    void addDeckToRegistration(EventRegistration registration);

}
