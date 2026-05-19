/*
package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollectionServiceTest {

    @Test
    void initializeEmptyCollection_shouldCreateCollection() {

        // Laver fake repository, så testen ikke bruger rigtig database
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service med fake repository
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Kalder metoden vi tester
        collectionService.initializeEmptyCollection(1);

        // Tjekker at repository bliver kaldt én gang med userId 1
        verify(collectionRepository, times(1)).createCollection(1);
    }

    @Test
    void addCardToCollection_shouldAddCard() {

        // Laver fake repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Kalder metoden med gyldige værdier
        collectionService.addCardToCollection(1, 5, null);

        // Tjekker at repository bliver kaldt korrekt
        verify(collectionRepository, times(1)).addCardToCollection(1, 5, null);
    }

    @Test
    void addCardToCollection_shouldThrowException_whenUserIdIsInvalid() {

        // Laver fake repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Tjekker at metoden kaster fejl ved ugyldigt userId
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {collectionService.addCardToCollection(0, 5, null);});

        // Tjekker fejlbeskeden
        assertEquals("Ugyldigt ID", exception.getMessage());

        // Tjekker at repository ikke bliver kaldt
        verify(collectionRepository, never()).addCardToCollection(anyInt(), anyInt(), any());
    }

    @Test
    void addCardToCollection_shouldThrowException_whenCardIdIsInvalid() {

        // Laver fake repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Tjekker at metoden kaster fejl ved ugyldigt cardId
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {collectionService.addCardToCollection(1, 0, null);});

        // Tjekker fejlbeskeden
        assertEquals("Ugyldigt ID", exception.getMessage());

        // Tjekker at repository ikke bliver kaldt
        verify(collectionRepository, never()).addCardToCollection(anyInt(), anyInt(), any());
    }

    @Test
    void removeCardFromCollection_shouldDeleteCard() {

        // Laver fake repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Kalder metoden
        collectionService.removeCardFromCollection(1, 5);

        // Tjekker at repository bliver kaldt korrekt
        verify(collectionRepository, times(1)).deleteCardFromCollection(1, 5);
    }

    @Test
    void getUserCollection_shouldReturnCards() {

        // Laver fake repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake liste med kort
        List<Card> cards = List.of(new Card(), new Card());

        // Siger hvad repository skal returnere
        when(collectionRepository.findUserCollection(1)).thenReturn(cards);

        // Kalder metoden
        List<Card> result = collectionService.getUserCollection(1);

        // Tjekker at listen har 2 kort
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt én gang
        verify(collectionRepository, times(1)).findUserCollection(1);
    }

    @Test
    void findUserCollection_shouldReturnCards() {

        // Laver fake repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake liste
        List<Card> cards = List.of(new Card());

        // Siger hvad repository skal returnere
        when(collectionRepository.findUserCollection(2)).thenReturn(cards);

        // Kalder metoden
        List<Card> result = collectionService.findUserCollection(2);

        // Tjekker at listen har 1 kort
        assertEquals(1, result.size());

        // Tjekker at repository blev kaldt korrekt
        verify(collectionRepository, times(1)).findUserCollection(2);
    }
}
 */