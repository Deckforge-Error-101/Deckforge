package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.Validation;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {
    private final IEventRepository eventRepository;
    private final Validation validation;

    public void createEvent(Event event) {
       //validation.validateCreateEvent(event);
        eventRepository.createEvent(event);
    }
    public EventService(IEventRepository eventRepository, Validation validation) {
        this.eventRepository = eventRepository;
        this.validation = validation;
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAllEvents();
    }

    public Event findEventById(int eventId) {
        return eventRepository.findById(eventId);
    }
}


