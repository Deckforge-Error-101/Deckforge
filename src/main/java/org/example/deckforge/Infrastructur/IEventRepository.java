package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Event;

import java.util.List;

public interface IEventRepository {
    void createEvent(Event event);
    void deleteEvent(Event event);
    void updateEvent(Event event);
    List<Event> findAllEvents();
    Event findByEvent(Event event);
    void updateStatus(Event event);
}
