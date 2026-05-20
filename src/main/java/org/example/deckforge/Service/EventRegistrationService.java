package org.example.deckforge.Service;

import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Domain.User;
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

    public EventRegistrationService(IEventRegistrationRepository eventRegistrationRepository,
                                    IEventRepository eventRepository,
                                    Validation validation) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRepository = eventRepository;
        this.validation = validation;
    }

    public void registerToEvent(Event event, User user) {

        EventRegistration registration = new EventRegistration();

        try {
            registration.setEventId(event.getEventId());
            registration.setUserId(user.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Fejl ved oprettelse af tilmelding");
        }

        registerToEvent(registration);
    }

    public void registerToEvent(EventRegistration registration) {
        try {
            Event searchEvent = new Event();
            searchEvent.setEventId(registration.getEventId());

            Event event = eventRepository.findByEvent(searchEvent);

            if (event == null) {
                throw new RuntimeException("Eventet findes ikke");
            }

            User user = new User();
            user.setUserId(registration.getUserId());

            if (eventRegistrationRepository.existsByEventAndUser(event, user)) {
                throw new RuntimeException("Du er allerede tilmeldt dette event");
            }
            int registrations = eventRegistrationRepository.countRegistrationsByEvent(event);

            if (registrations >= event.getCapacity()) {
                event.setStatusType("FULL");
                eventRepository.updateStatus(event);

                throw new RuntimeException("Eventet er fyldt");
            }

            registration.setRegistrationDate(LocalDateTime.now());

            eventRegistrationRepository.createRegistration(registration);

            registrations = eventRegistrationRepository.countRegistrationsByEvent(event);

            if (registrations >= event.getCapacity()) {
                event.setStatusType("FULL");
            } else {
                event.setStatusType("OPEN");
            }

            eventRepository.updateStatus(event);

        } catch (DataAccessException dae) {
            throw new EventException("Fejl ved registrering til event");
        } catch (Exception e) {
            throw new RuntimeException("Kritisk fejl: " + e.getMessage());
        }
    }

    public void addDeckToRegistration(EventRegistration registration, Deck deck) {

        EventRegistration dbRegistration = eventRegistrationRepository.findByRegistration(registration);

        if (dbRegistration == null) {
            throw new RuntimeException("Tilmeldingen findes ikke");
        }

        if (dbRegistration.getUserId() != registration.getUserId()) {
            throw new RuntimeException("Du kan kun ændre dine egne tilmeldinger");
        }

        Event searchEvent = new Event();
        searchEvent.setEventId(dbRegistration.getEventId());
        Event event = eventRepository.findByEvent(searchEvent);

        if (event == null) {
            throw new RuntimeException("Det tilknyttede event blev ikke fundet");
        }

        validation.validateRegisterDeck(dbRegistration, deck, event);

        dbRegistration.setDeckId(deck.getDeckId());
        eventRegistrationRepository.addDeckToRegistration(dbRegistration);
    }

    public void unregisterFromEvent(EventRegistration registration) {

        EventRegistration dbRegistration =
                eventRegistrationRepository.findByRegistration(registration);

        if (dbRegistration == null) {
            throw new RuntimeException("Tilmeldingen findes ikke");
        }

        if (dbRegistration.getUserId() != registration.getUserId()) {
            throw new RuntimeException("Du kan kun framelde dine egne tilmeldinger");
        }

        eventRegistrationRepository.deleteRegistration(registration);
        // Opretter et midlertidigt Event-objekt med eventId fra den slettede tilmelding.
        // Det bruges til at hente det fulde event fra databasen.
        Event searchEvent = new Event();
        searchEvent.setEventId(dbRegistration.getEventId());

        // Henter eventet fra databasen, så vi kan få capacity og nuværende status
        Event event = eventRepository.findByEvent(searchEvent);
        // Tæller hvor mange brugere der stadig er tilmeldt eventet efter frameldinge
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
        // Gemmer den opdaterede status i Events-tabellen
        eventRepository.updateStatus(event);

    }

    public List<EventRegistration> findRegistrationsByUser(User user) {
        return eventRegistrationRepository.findAllByUser(user);
    }
}