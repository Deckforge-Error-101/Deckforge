package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final IEventRepository eventRepository;
    private final IEventRegistrationRepository registrationRepository;

    public EventService(IEventRepository eventRepository,
                        IEventRegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAllEvents();
    }

    public Event findEventById(int eventId) {
        return eventRepository.findById(eventId);
    }

    public void registerToEvent(int eventId, int userId, int deckId) {
        Event event = eventRepository.findById(eventId);

        if (event == null) {
            throw new RuntimeException("Eventet findes ikke");
        }

        EventRegistration registration = new EventRegistration();
        try {
        registration.setEventId(eventId);
        registration.setUserId(userId);
        registration.setDeckId(deckId);


            registration.setRegistrationDate(LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException("Der kunne ikke sættes registreringsdato");
        }

        registrationRepository.createRegistration(registration);
    }

    public void unregisterFromEvent(int registrationId, int userId) {
        EventRegistration registration = registrationRepository.findById(registrationId);

        if (registration == null) {
            throw new RuntimeException("Tilmeldingen findes ikke");
        }

        if (registration.getUserId() != userId) {
            throw new RuntimeException("Du kan kun framelde dine egne tilmeldinger");
        }

        registrationRepository.deleteRegistration(registrationId);
    }

    public List<EventRegistration> findRegistrationsByUserId(int userId) {
        return registrationRepository.findAllByUserId(userId);
    }
}
