package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Event;

import java.util.List;

public interface IEventRepository {
    void createEvent(Event event);
    void deleteEvent(int eventId);
    void updateEvent(int eventId);
    List<Event> findAllEvents();
    Event findById(int eventId);
}
