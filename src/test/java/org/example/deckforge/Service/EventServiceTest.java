package org.example.deckforge.Service;

import org.junit.jupiter.api.Test;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Infrastructur.IEventRepository;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    @Test
    void createEvent() throws Exception {
        //laver fake/mock version af repo
        IEventRepository eventRepository = mock(IEventRepository.class);
        //opretter eventservice via mocks, Dependency intjection ligsom spring gør det
        EventService eventService = new EventService(eventRepository);
    }

    @Test
    void findAllEvents() {
    }

    @Test
    void findEventById() {
    }
}