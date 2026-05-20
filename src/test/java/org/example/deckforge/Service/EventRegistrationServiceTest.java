/*
package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.Validation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventRegistrationServiceTest {

    @Test
    void registerToEvent_shouldCreateRegistration() throws Exception {

        // Laver fake repositories og validation
        IEventRegistrationRepository registrationRepository = mock(IEventRegistrationRepository.class);

        IEventRepository eventRepository = mock(IEventRepository.class);

        Validation validation = mock(Validation.class);

        // Opretter service med mocks
        EventRegistrationService service = new EventRegistrationService(registrationRepository, eventRepository, validation);

        // Opretter en registration
        EventRegistration registration = new EventRegistration();

        // Sætter eventId
        registration.setEventId(1);

        // Sætter userId
        registration.setUserId(2);

        // Opretter fake event
        Event event = new Event();

        // Siger at eventet findes
        when(eventRepository.findById(1)).thenReturn(event);

        // Siger at brugeren ikke allerede er tilmeldt
        when(registrationRepository.existsByEventIdAndUserId(1, 2)).thenReturn(false);

        // Kalder metoden
        service.registerToEvent(registration);

        // Tjekker at registration blev gemt
        verify(registrationRepository, times(1)).createRegistration(registration);
    }

    @Test
    void registerToEvent_shouldThrowException_whenUserAlreadyRegistered() throws Exception {

        // Laver fake repositories og validation
        IEventRegistrationRepository registrationRepository = mock(IEventRegistrationRepository.class);

        IEventRepository eventRepository = mock(IEventRepository.class);

        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(registrationRepository, eventRepository, validation);

        // Opretter registration
        EventRegistration registration = new EventRegistration();

        // Sætter eventId
        registration.setEventId(1);

        // Sætter userId
        registration.setUserId(2);

        // Siger at eventet findes
        when(eventRepository.findById(1)).thenReturn(new Event());

        // Siger at brugeren allerede er tilmeldt
        when(registrationRepository.existsByEventIdAndUserId(1, 2)).thenReturn(true);

        // Tjekker at der bliver kastet exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {service.registerToEvent(registration);});

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl", exception.getMessage());

        // Tjekker at registration IKKE bliver gemt
        verify(registrationRepository, never()).createRegistration(any(EventRegistration.class));
    }

    @Test
    void addDeckToRegistration_shouldAddDeck() throws Exception {

        // Laver fake repositories og validation
        IEventRegistrationRepository registrationRepository = mock(IEventRegistrationRepository.class);

        IEventRepository eventRepository = mock(IEventRepository.class);

        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(registrationRepository, eventRepository, validation);

        // Opretter input registration
        EventRegistration registration = new EventRegistration();

        // Sætter registrationId
        registration.setRegistrationId(1);

        // Sætter userId
        registration.setUserId(2);

        // Sætter deckId
        registration.setDeckId(5);

        // Opretter database registration
        EventRegistration dbRegistration = new EventRegistration();

        // Sætter samme userId, så brugeren ejer tilmeldingen
        dbRegistration.setUserId(2);

        // Siger at registration findes i databasen
        when(registrationRepository.findById(1))
                .thenReturn(dbRegistration);

        // Kalder metoden
        service.addDeckToRegistration(registration);

        // Tjekker at deck bliver tilføjet til registration
        verify(registrationRepository, times(1)).addDeckToRegistration(1, 5);
    }

    @Test
    void unregisterFromEvent_shouldDeleteRegistration() throws Exception {

        // Laver fake repositories og validation
        IEventRegistrationRepository registrationRepository = mock(IEventRegistrationRepository.class);

        IEventRepository eventRepository = mock(IEventRepository.class);

        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(registrationRepository, eventRepository, validation);

        // Opretter input registration
        EventRegistration registration = new EventRegistration();

        // Sætter registrationId
        registration.setRegistrationId(1);

        // Sætter userId
        registration.setUserId(2);

        // Opretter registration fra database
        EventRegistration dbRegistration = new EventRegistration();

        // Sætter samme userId
        dbRegistration.setUserId(2);

        // Siger at registration findes
        when(registrationRepository.findById(1)).thenReturn(dbRegistration);

        // Kalder metoden
        service.unregisterFromEvent(registration);

        // Tjekker at registration bliver slettet
        verify(registrationRepository, times(1)).deleteRegistration(1);
    }

    @Test
    void findRegistrationsByUserId_shouldReturnList() {

        // Laver fake repositories og validation
        IEventRegistrationRepository registrationRepository = mock(IEventRegistrationRepository.class);

        IEventRepository eventRepository = mock(IEventRepository.class);

        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(registrationRepository, eventRepository, validation);

        // Laver fake liste
        List<EventRegistration> registrations = List.of(new EventRegistration(), new EventRegistration());

        // Siger hvad repository skal returnere
        when(registrationRepository.findAllByUserId(2)).thenReturn(registrations);

        // Kalder metoden
        List<EventRegistration> result = service.findRegistrationsByUserId(2);

        // Tjekker at listen har 2 elementer
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(registrationRepository, times(1)).findAllByUserId(2);
    }
}

 */