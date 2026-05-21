package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.EventException;
import org.example.deckforge.Service.Validation.Validation;
import org.springframework.dao.DataAccessException;
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

    public void createEvent(User user, Event event) {
        validation.validateCreateEvent(event, user);
        try {
            eventRepository.createEvent(event);
        } catch (DataAccessException | EventException dae) {
            throw new EventException("Der er sket en fejl ved oprettelse af event, prøv igen senere");
        } catch (Exception e) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void deleteEvent(Event event) {
        validation.validateDeleteEvent(event);
        try {
            eventRepository.deleteEvent(event);
        } catch (DataAccessException | EventException dae) {
            throw new EventException("Fejl ved sletning af event, prøv igen senere");
        } catch (Exception e) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void updateEvent(User user, Event event) {
        validation.validateCreateEvent(event, user);
        eventRepository.updateEvent(event);
        try {
            updateStatus(event);
        } catch (DataAccessException | EventException dae) {
            throw new EventException("Fejl ved opdatering af event, prøv igen senere");
        } catch (Exception e) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public List<Event> findAllEvents() {
        try {
            return eventRepository.findAllEvents();
        } catch (DataAccessException | EventException dae) {
            throw new EventException("Fejl ved events, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public Event findEvent(Event event) {
        try {
            return eventRepository.findByEvent(event);
        } catch (DataAccessException | EventException dae) {
            throw new EventException("Fejl ved events, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void updateStatus(Event event) {
        int registrations = eventRegistrationRepository.countRegistrationsByEvent(event);

        try {
            if (registrations >= event.getCapacity()) {
                event.setStatusType("FULL");
            } else {
                event.setStatusType("OPEN");
            }

            eventRepository.updateStatus(event);

        } catch (DataAccessException | EventException dae) {
            throw new EventException("Fejl ved events, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }
}


