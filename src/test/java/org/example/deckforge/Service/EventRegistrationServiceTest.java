package org.example.deckforge.Service;

import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.IDeckRepository;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.DeckException;
import org.example.deckforge.Service.Validation.EventException;
import org.example.deckforge.Service.Validation.Validation;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventRegistrationServiceTest {

    // Test af registerToEvent()
    @Test
    void registerToEvent_shouldCreateRegistration_whenEventIsOpen() throws Exception {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock registration og event
        EventRegistration registration = mock(EventRegistration.class);
        Event event = mock(Event.class);

        // Simulerer ID'er
        when(registration.getEventId()).thenReturn(1);
        when(registration.getUserId()).thenReturn(2);

        // Bestemmer hvad repository skal returnere
        when(eventRepository.findByEvent(any(Event.class))).thenReturn(event);
        when(eventRegistrationRepository.existsByEventAndUser(eq(event), any(User.class))).thenReturn(false);
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(1);
        when(event.getCapacity()).thenReturn(5);

        // Kalder metoden
        service.registerToEvent(registration);

        // Tjekker at registrationDate bliver sat
        verify(registration, times(1)).setRegistrationDate(any(LocalDateTime.class));

        // Tjekker at tilmeldingen bliver oprettet
        verify(eventRegistrationRepository, times(1)).createRegistration(registration);

        // Tjekker at event-status bliver sat til OPEN
        verify(event, times(1)).setStatusType("OPEN");

        // Tjekker at status bliver opdateret
        verify(eventRepository, times(1)).updateStatus(event);
    }

    // Test af registerToEvent() når eventet er fyldt
    @Test
    void registerToEvent_shouldThrowException_whenEventIsFull() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock registration og event
        EventRegistration registration = mock(EventRegistration.class);
        Event event = mock(Event.class);

        // Simulerer ID'er
        when(registration.getEventId()).thenReturn(1);
        when(registration.getUserId()).thenReturn(2);

        // Bestemmer hvad repository skal returnere
        when(eventRepository.findByEvent(any(Event.class))).thenReturn(event);
        when(eventRegistrationRepository.existsByEventAndUser(eq(event), any(User.class))).thenReturn(false);
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(5);
        when(event.getCapacity()).thenReturn(5);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.registerToEvent(registration);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl: Eventet er fyldt", exception.getMessage());

        // Tjekker at status bliver sat til FULL
        verify(event, times(1)).setStatusType("FULL");

        // Tjekker at registration IKKE bliver oprettet
        verify(eventRegistrationRepository, never()).createRegistration(any());
    }

    // Test af registerToEvent() når brugeren allerede er tilmeldt
    @Test
    void registerToEvent_shouldThrowException_whenUserAlreadyRegistered() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock registration og event
        EventRegistration registration = mock(EventRegistration.class);
        Event event = mock(Event.class);

        // Simulerer ID'er
        when(registration.getEventId()).thenReturn(1);
        when(registration.getUserId()).thenReturn(2);

        // Bestemmer hvad repository skal returnere
        when(eventRepository.findByEvent(any(Event.class))).thenReturn(event);
        when(eventRegistrationRepository.existsByEventAndUser(eq(event), any(User.class))).thenReturn(true);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.registerToEvent(registration);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl: Du er allerede tilmeldt dette event", exception.getMessage());

        // Tjekker at registration IKKE bliver oprettet
        verify(eventRegistrationRepository, never()).createRegistration(any());
    }

    // Test af registerToEvent() ved databasefejl
    @Test
    void registerToEvent_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock registration
        EventRegistration registration = mock(EventRegistration.class);

        // Simulerer ID'er
        when(registration.getEventId()).thenReturn(1);

        // Simulerer databasefejl
        when(eventRepository.findByEvent(any(Event.class))).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            service.registerToEvent(registration);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved registrering til event", exception.getMessage());
    }

    // Test af addDeckToRegistration()
    @Test
    void addDeckToRegistration_shouldCallRepository_whenDataIsValid() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock objekter
        EventRegistration registration = mock(EventRegistration.class);
        EventRegistration dbRegistration = mock(EventRegistration.class);
        Event event = mock(Event.class);
        Deck deck = mock(Deck.class);
        Deck fullDeck = mock(Deck.class);

        // Simulerer ID'er
        when(registration.getUserId()).thenReturn(2);
        when(dbRegistration.getUserId()).thenReturn(2);
        when(dbRegistration.getEventId()).thenReturn(1);
        when(deck.getDeckId()).thenReturn(10);

        // Bestemmer hvad repositories skal returnere
        when(eventRegistrationRepository.findByRegistration(registration)).thenReturn(dbRegistration);
        when(eventRepository.findByEvent(any(Event.class))).thenReturn(event);
        when(deckRepository.findDeckById(deck)).thenReturn(fullDeck);

        // Kalder metoden
        service.addDeckToRegistration(registration, deck);

        // Tjekker at deckId bliver sat
        verify(dbRegistration, times(1)).setDeckId(10);

        // Tjekker at validation bliver kaldt
        verify(validation, times(1)).validateRegisterDeck(dbRegistration, fullDeck, event);

        // Tjekker at repository bliver kaldt
        verify(eventRegistrationRepository, times(1)).addDeckToRegistration(dbRegistration);
    }

    // Test af addDeckToRegistration() når registration ikke findes
    @Test
    void addDeckToRegistration_shouldThrowException_whenRegistrationDoesNotExist() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock objekter
        EventRegistration registration = mock(EventRegistration.class);
        Deck deck = mock(Deck.class);

        // Simulerer at registration ikke findes
        when(eventRegistrationRepository.findByRegistration(registration)).thenReturn(null);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.addDeckToRegistration(registration, deck);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl kontakt en administratorTilmeldingen findes ikke", exception.getMessage());

        // Tjekker at deck ikke bliver tilføjet
        verify(eventRegistrationRepository, never()).addDeckToRegistration(any());
    }

    // Test af addDeckToRegistration() når deck ikke findes
    @Test
    void addDeckToRegistration_shouldThrowDeckException_whenDeckDoesNotExist() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock objekter
        EventRegistration registration = mock(EventRegistration.class);
        EventRegistration dbRegistration = mock(EventRegistration.class);
        Event event = mock(Event.class);
        Deck deck = mock(Deck.class);

        // Simulerer ID'er
        when(registration.getUserId()).thenReturn(2);
        when(dbRegistration.getUserId()).thenReturn(2);
        when(dbRegistration.getEventId()).thenReturn(1);

        // Bestemmer hvad repositories skal returnere
        when(eventRegistrationRepository.findByRegistration(registration)).thenReturn(dbRegistration);
        when(eventRepository.findByEvent(any(Event.class))).thenReturn(event);
        when(deckRepository.findDeckById(deck)).thenReturn(null);

        // Tjekker at DeckException bliver kastet
        DeckException exception = assertThrows(DeckException.class, () -> {
            service.addDeckToRegistration(registration, deck);
        });

        // Tjekker fejlbeskeden
        assertEquals("Det indtastede dæk-ID eksisterer ikke.", exception.getMessage());

        // Tjekker at deck ikke bliver tilføjet
        verify(eventRegistrationRepository, never()).addDeckToRegistration(any());
    }

    // Test af addDeckToRegistration() ved databasefejl
    @Test
    void addDeckToRegistration_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock objekter
        EventRegistration registration = mock(EventRegistration.class);
        Deck deck = mock(Deck.class);

        // Simulerer databasefejl
        when(eventRegistrationRepository.findByRegistration(registration)).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            service.addDeckToRegistration(registration, deck);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved registrering til event, prøv igen senere", exception.getMessage());
    }

    // Test af unregisterFromEvent()
    @Test
    void unregisterFromEvent_shouldDeleteRegistrationAndUpdateStatus() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock objekter
        EventRegistration registration = mock(EventRegistration.class);
        EventRegistration dbRegistration = mock(EventRegistration.class);
        Event event = mock(Event.class);

        // Simulerer ID'er
        when(registration.getUserId()).thenReturn(2);
        when(dbRegistration.getUserId()).thenReturn(2);
        when(dbRegistration.getEventId()).thenReturn(1);

        // Bestemmer hvad repositories skal returnere
        when(eventRegistrationRepository.findByRegistration(registration)).thenReturn(dbRegistration);
        when(eventRepository.findByEvent(any(Event.class))).thenReturn(event);
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(3);
        when(event.getCapacity()).thenReturn(5);

        // Kalder metoden
        service.unregisterFromEvent(registration);

        // Tjekker at registration bliver slettet
        verify(eventRegistrationRepository, times(1)).deleteRegistration(registration);

        // Tjekker at status bliver sat til OPEN
        verify(event, times(1)).setStatusType("OPEN");

        // Tjekker at status bliver opdateret
        verify(eventRepository, times(1)).updateStatus(event);
    }

    // Test af unregisterFromEvent() når registration ikke findes
    @Test
    void unregisterFromEvent_shouldThrowException_whenRegistrationDoesNotExist() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock registration
        EventRegistration registration = mock(EventRegistration.class);

        // Simulerer at registration ikke findes
        when(eventRegistrationRepository.findByRegistration(registration)).thenReturn(null);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.unregisterFromEvent(registration);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt administrator", exception.getMessage());

        // Tjekker at registration IKKE bliver slettet
        verify(eventRegistrationRepository, never()).deleteRegistration(any());
    }

    // Test af unregisterFromEvent() ved databasefejl
    @Test
    void unregisterFromEvent_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock registration
        EventRegistration registration = mock(EventRegistration.class);

        // Simulerer databasefejl
        when(eventRegistrationRepository.findByRegistration(registration))
                .thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            service.unregisterFromEvent(registration);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved Event, prøv igen senere", exception.getMessage());
    }

    // Test af findRegistrationsByUser()
    @Test
    void findRegistrationsByUser_shouldReturnRegistrations() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Laver fake liste med 2 registrations
        List<EventRegistration> registrations = List.of(mock(EventRegistration.class), mock(EventRegistration.class));

        // Bestemmer hvad repository skal returnere
        when(eventRegistrationRepository.findAllByUser(user)).thenReturn(registrations);

        // Kalder metoden
        List<EventRegistration> result = service.findRegistrationsByUser(user);

        // Tjekker at listen indeholder 2 registrations
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(eventRegistrationRepository, times(1)).findAllByUser(user);
    }

    // Test af findRegistrationsByUser() ved databasefejl
    @Test
    void findRegistrationsByUser_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        IEventRepository eventRepository = mock(IEventRepository.class);
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventRegistrationService service = new EventRegistrationService(eventRegistrationRepository, eventRepository, deckRepository, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer databasefejl
        when(eventRegistrationRepository.findAllByUser(user)).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            service.findRegistrationsByUser(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved Event, prøv igen senere", exception.getMessage());
    }
}