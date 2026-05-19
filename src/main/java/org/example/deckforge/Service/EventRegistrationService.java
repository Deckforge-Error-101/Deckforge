package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.EventException;
import org.example.deckforge.Service.Validation.Validation;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventRegistrationService {
    private final IEventRegistrationRepository eventRegistrationRepository;
    private final IEventRepository eventRepository;
    private final Validation validation;

    public EventRegistrationService(IEventRegistrationRepository eventRegistrationRepository, IEventRepository eventRepository, Validation validation) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRepository = eventRepository;
        this.validation = validation;
    }


    public void registerToEvent(EventRegistration registration) {
        try {
            Event event = eventRepository.findById(registration.getEventId());

            if (event == null) {
                throw new RuntimeException("Eventet findes ikke");
            }

            if (eventRegistrationRepository.existsByEventIdAndUserId(registration.getEventId(), registration.getUserId())) {
                throw new RuntimeException(
                        "Du er allerede tilmeldt dette event");
            }

            registration.setRegistrationDate(LocalDateTime.now());
        } catch (DataAccessException dae) {
            throw new EventException("Fejl ved registreringsdato");
        } catch (Exception e) {
            throw new RuntimeException("Kritisk fejl" + e.getMessage());
        }

        eventRegistrationRepository.createRegistration(registration);
    }


    public void addDeckToRegistration(EventRegistration registration) {

        EventRegistration dbRegistration =
                eventRegistrationRepository.findById(registration.getRegistrationId());

        if (dbRegistration == null) {
            throw new RuntimeException("Tilmeldingen findes ikke");
        }

        if (dbRegistration.getUserId() != registration.getUserId()) {
            throw new RuntimeException("Du kan kun ændre dine egne tilmeldinger");
        }
        eventRegistrationRepository.addDeckToRegistration(registration.getRegistrationId(), registration.getDeckId());
    }

    public void unregisterFromEvent(EventRegistration registration) {

        EventRegistration dbRegistration = eventRegistrationRepository.findById(registration.getRegistrationId());

        if (dbRegistration == null) {
            throw new RuntimeException("Tilmeldingen findes ikke");
        }

        if (dbRegistration.getUserId() != registration.getUserId()) {
            throw new RuntimeException("Du kan kun framelde dine egne tilmeldinger");
        }

        eventRegistrationRepository.deleteRegistration(registration.getRegistrationId());
    }

    public List<EventRegistration> findRegistrationsByUserId(int userId) {
        return eventRegistrationRepository.findAllByUserId(userId);
    }
}

