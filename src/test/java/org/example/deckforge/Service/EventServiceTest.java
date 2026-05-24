package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.IEventRegistrationRepository;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.EventException;
import org.example.deckforge.Service.Validation.Validation;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    // Test af createEvent()
    @Test
    void createEvent_shouldCallRepository() throws Exception {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock user og event
        User user = mock(User.class);
        Event event = mock(Event.class);

        // Kalder metoden
        eventService.createEvent(user, event);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateCreateEvent(event, user);

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).createEvent(event);
    }

    // Test af createEvent() ved databasefejl
    @Test
    void createEvent_shouldThrowEventException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock user og event
        User user = mock(User.class);
        Event event = mock(Event.class);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(eventRepository).createEvent(event);

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.createEvent(user, event);
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er sket en fejl ved oprettelse af event, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).createEvent(event);
    }

    // Test af deleteEvent()
    @Test
    void deleteEvent_shouldCallRepository() throws Exception {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock event
        Event event = mock(Event.class);

        // Kalder metoden
        eventService.deleteEvent(event);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateDeleteEvent(event);

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).deleteEvent(event);
    }

    // Test af deleteEvent() ved databasefejl
    @Test
    void deleteEvent_shouldThrowEventException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock event
        Event event = mock(Event.class);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(eventRepository).deleteEvent(event);

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.deleteEvent(event);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved sletning af event, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).deleteEvent(event);
    }

    // Test af updateEvent()
    @Test
    void updateEvent_shouldCallRepositoryAndUpdateStatus() throws Exception {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock user og event
        User user = mock(User.class);
        Event event = mock(Event.class);

        // Simulerer antal tilmeldinger og capacity
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(2);
        when(event.getCapacity()).thenReturn(5);

        // Kalder metoden
        eventService.updateEvent(user, event);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateCreateEvent(event, user);

        // Tjekker at event blev opdateret
        verify(eventRepository, times(1)).updateEvent(event);

        // Tjekker at status blev sat til OPEN
        verify(event, times(1)).setStatusType("OPEN");

        // Tjekker at status blev opdateret
        verify(eventRepository, times(1)).updateStatus(event);
    }

    // Test af findAllEvents()
    @Test
    void findAllEvents_shouldReturnEvents() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake liste med 2 events
        List<Event> events = List.of(mock(Event.class), mock(Event.class));

        // Bestemmer hvad repository skal returnere
        when(eventRepository.findAllEvents()).thenReturn(events);

        // Kalder metoden
        List<Event> result = eventService.findAllEvents();

        // Tjekker at listen indeholder 2 events
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).findAllEvents();
    }

    // Test af findAllEvents() ved databasefejl
    @Test
    void findAllEvents_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Simulerer databasefejl
        when(eventRepository.findAllEvents()).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.findAllEvents();
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved events, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).findAllEvents();
    }

    // Test af findEvent()
    @Test
    void findEvent_shouldReturnEvent() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock events
        Event searchEvent = mock(Event.class);
        Event foundEvent = mock(Event.class);

        // Bestemmer hvad repository skal returnere
        when(eventRepository.findByEvent(searchEvent)).thenReturn(foundEvent);

        // Kalder metoden
        Event result = eventService.findEvent(searchEvent);

        // Tjekker at det rigtige event bliver returneret
        assertEquals(foundEvent, result);

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).findByEvent(searchEvent);
    }

    // Test af findEvent() ved databasefejl
    @Test
    void findEvent_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock event
        Event event = mock(Event.class);

        // Simulerer databasefejl
        when(eventRepository.findByEvent(event)).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.findEvent(event);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved events, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).findByEvent(event);
    }

    // Test af updateStatus() når event ikke er fyldt
    @Test
    void updateStatus_shouldSetStatusOpen_whenRegistrationsAreBelowCapacity() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock event
        Event event = mock(Event.class);

        // Simulerer antal tilmeldinger og capacity
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(2);
        when(event.getCapacity()).thenReturn(5);

        // Kalder metoden
        eventService.updateStatus(event);

        // Tjekker at status bliver sat til OPEN
        verify(event, times(1)).setStatusType("OPEN");

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).updateStatus(event);
    }

    // Test af updateStatus() når event er fyldt
    @Test
    void updateStatus_shouldSetStatusFull_whenRegistrationsAreEqualToCapacity() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock event
        Event event = mock(Event.class);

        // Simulerer antal tilmeldinger og capacity
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(5);
        when(event.getCapacity()).thenReturn(5);

        // Kalder metoden
        eventService.updateStatus(event);

        // Tjekker at status bliver sat til FULL
        verify(event, times(1)).setStatusType("FULL");

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).updateStatus(event);
    }

    // Test af updateStatus() ved databasefejl
    @Test
    void updateStatus_shouldThrowEventException_whenRepositoryFails() {

        // Laver fake/mock repositories og validation
        IEventRepository eventRepository = mock(IEventRepository.class);
        IEventRegistrationRepository eventRegistrationRepository = mock(IEventRegistrationRepository.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, eventRegistrationRepository, validation);

        // Laver fake/mock event
        Event event = mock(Event.class);

        // Simulerer antal tilmeldinger og capacity
        when(eventRegistrationRepository.countRegistrationsByEvent(event)).thenReturn(2);
        when(event.getCapacity()).thenReturn(5);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(eventRepository).updateStatus(event);

        // Tjekker at EventException bliver kastet
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.updateStatus(event);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved events, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).updateStatus(event);
    }
}