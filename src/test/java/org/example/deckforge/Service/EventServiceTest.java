package org.example.deckforge.Service;

import org.example.deckforge.Domain.Event;
import org.example.deckforge.Infrastructur.IEventRepository;
import org.example.deckforge.Service.Validation.Validation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Test
    void createEvent_shouldCallRepository() {

        // Laver fake repository
        IEventRepository eventRepository = mock(IEventRepository.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service med fake dependencies
        EventService eventService = new EventService(eventRepository, validation);

        // Opretter event objekt
        Event event = new Event();

        // Kalder metoden vi tester
        eventService.createEvent(event);

        // Tjekker at repository.createEvent bliver kaldt én gang
        verify(eventRepository, times(1)).createEvent(event);
    }

    @Test
    void findAllEvents_shouldReturnEvents() {

        // Laver fake repository
        IEventRepository eventRepository = mock(IEventRepository.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, validation);

        // Laver fake liste
        List<Event> events = List.of(new Event(), new Event());

        // Bestemmer hvad repository skal returnere
        when(eventRepository.findAllEvents()).thenReturn(events);

        // Kalder metoden
        List<Event> result = eventService.findAllEvents();

        // Tjekker at listen har 2 events
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(eventRepository, times(1)).findAllEvents();
    }

    @Test
    void findEventById_shouldReturnEvent() {

        // Laver fake repository
        IEventRepository eventRepository = mock(IEventRepository.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        EventService eventService = new EventService(eventRepository, validation);

        // Opretter fake event
        Event event = new Event();

        // Bestemmer at repository skal returnere event ved id 1
        when(eventRepository.findById(1)).thenReturn(event);

        // Kalder metoden
        Event result = eventService.findEventById(1);

        // Tjekker at resultatet ikke er null
        assertNotNull(result);

        // Tjekker at repository blev kaldt med id 1
        verify(eventRepository, times(1)).findById(1);
    }
}