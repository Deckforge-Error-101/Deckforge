package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.Validation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {
    private final IEventRepository eventRepository;
    private final IEventRegistrationRepository eventRegistrationRepository;
    private final Validation validation;

    public EventService(IEventRepository eventRepository, IEventRegistrationRepository eventRegistrationRepository, Validation validation) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.validation = validation;
    }

    public void createEvent(Event event) {
       //validation.validateCreateEvent(event);
        eventRepository.createEvent(event);
    }
    public void deleteEvent(Event event) {

        eventRepository.deleteEvent(event);
    }
    public void updateEvent(Event event) {
        eventRepository.updateEvent(event);
        updateStatus(event);
    }


    public List<Event> findAllEvents() {
        return eventRepository.findAllEvents();
    }

    public Event findEvent(Event event) {
        return eventRepository.findByEvent(event);
    }

    public void updateStatus(Event event) {
        int registrations = eventRegistrationRepository.countRegistrationsByEvent(event);

        try {
            if (registrations >= event.getCapacity()) {
                event.setStatusType("FULL");
            } else {
                event.setStatusType("OPEN");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        eventRepository.updateStatus(event);
    }
}


