package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.Collection;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Service.Validation.CollectionException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollectionServiceTest {

    // Test af initializeEmptyCollection()
    @Test
    void initializeEmptyCollection_shouldCallRepository() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Kalder metoden
        collectionService.initializeEmptyCollection(1);

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).createCollection(1);
    }

    // Test af initializeEmptyCollection() ved databasefejl
    @Test
    void initializeEmptyCollection_shouldThrowCollectionException_whenRepositoryFails() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(collectionRepository).createCollection(1);

        // Tjekker at CollectionException bliver kastet
        CollectionException exception = assertThrows(CollectionException.class, () -> {
            collectionService.initializeEmptyCollection(1);
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er skete en fejl ved indlæsning af collection, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).createCollection(1);
    }

    // Test af addCardToCollection()
    @Test
    void addCardToCollection_shouldCallRepository_whenIdsAreValid() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Opretter collection objekt
        Collection collection = new Collection();

        // Simulerer gyldige ID'er
        when(user.getUserId()).thenReturn(1);
        when(card.getCardId()).thenReturn(10);

        // Kalder metoden
        collectionService.addCardToCollection(user, card, collection);

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).addCardToCollection(user, card, collection);
    }

    // Test af addCardToCollection() med ugyldigt userId
    @Test
    void addCardToCollection_shouldThrowException_whenUserIdIsInvalid() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Opretter collection objekt
        Collection collection = new Collection();

        // Simulerer ugyldigt userId
        when(user.getUserId()).thenReturn(0);
        when(card.getCardId()).thenReturn(10);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            collectionService.addCardToCollection(user, card, collection);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(collectionRepository, never()).addCardToCollection(any(), any(), any());
    }

    // Test af addCardToCollection() med ugyldigt cardId
    @Test
    void addCardToCollection_shouldThrowException_whenCardIdIsInvalid() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Opretter collection objekt
        Collection collection = new Collection();

        // Simulerer ugyldigt cardId
        when(user.getUserId()).thenReturn(1);
        when(card.getCardId()).thenReturn(0);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            collectionService.addCardToCollection(user, card, collection);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(collectionRepository, never()).addCardToCollection(any(), any(), any());
    }

    // Test af addCardToCollection() ved databasefejl
    @Test
    void addCardToCollection_shouldThrowCollectionException_whenRepositoryFails() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Opretter collection objekt
        Collection collection = new Collection();

        // Simulerer gyldige ID'er
        when(user.getUserId()).thenReturn(1);
        when(card.getCardId()).thenReturn(10);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(collectionRepository).addCardToCollection(user, card, collection);

        // Tjekker at CollectionException bliver kastet
        CollectionException exception = assertThrows(CollectionException.class, () -> {
            collectionService.addCardToCollection(user, card, collection);
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er skete en fejl ved tilføjelse af kort, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).addCardToCollection(user, card, collection);
    }

    // Test af removeCardFromCollection()
    @Test
    void removeCardFromCollection_shouldCallRepository() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Kalder metoden
        collectionService.removeCardFromCollection(user, card);

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).deleteCardFromCollection(user, card);
    }

    // Test af getUserCollection()
    @Test
    void getUserCollection_shouldReturnCards() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user
        User user = mock(User.class);

        // Laver fake liste med 2 kort
        List<Card> cards = List.of(mock(Card.class), mock(Card.class));

        // Bestemmer hvad repository skal returnere
        when(collectionRepository.findUserCollection(user)).thenReturn(cards);

        // Kalder metoden
        List<Card> result = collectionService.getUserCollection(user);

        // Tjekker at listen indeholder 2 kort
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).findUserCollection(user);
    }

    // Test af findUserCollection()
    @Test
    void findUserCollection_shouldReturnCards() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user
        User user = mock(User.class);

        // Laver fake liste med 2 kort
        List<Card> cards = List.of(mock(Card.class), mock(Card.class));

        // Bestemmer hvad repository skal returnere
        when(collectionRepository.findUserCollection(user)).thenReturn(cards);

        // Kalder metoden
        List<Card> result = collectionService.findUserCollection(user);

        // Tjekker at listen indeholder 2 kort
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).findUserCollection(user);
    }

    // Test af getAllAvailableTrades()
    @Test
    void getAllAvailableTrades_shouldReturnTrades() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake liste med 2 trades
        List<CardToTrade> trades = List.of(mock(CardToTrade.class), mock(CardToTrade.class));

        // Bestemmer hvad repository skal returnere
        when(collectionRepository.findAllAvailableTrades()).thenReturn(trades);

        // Kalder metoden
        List<CardToTrade> result = collectionService.getAllAvailableTrades();

        // Tjekker at listen indeholder 2 trades
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(collectionRepository, times(1)).findAllAvailableTrades();
    }

    // Test af setCardAsTradeable()
    @Test
    void setCardAsTradeable_shouldCallRepository_whenIdsAreValid() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Simulerer gyldige ID'er
        when(user.getUserId()).thenReturn(1);
        when(card.getCardId()).thenReturn(10);

        // Kalder metoden
        collectionService.setCardAsTradeable(user, card);

        // Tjekker at repository blev kaldt med ACTIVE
        verify(collectionRepository, times(1)).updateTradeId(user, card, "ACTIVE");
    }

    // Test af setCardAsTradeable() med ugyldigt ID
    @Test
    void setCardAsTradeable_shouldThrowException_whenIdIsInvalid() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Simulerer ugyldigt ID
        when(user.getUserId()).thenReturn(0);
        when(card.getCardId()).thenReturn(10);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            collectionService.setCardAsTradeable(user, card);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(collectionRepository, never()).updateTradeId(any(), any(), any());
    }

    // Test af removeCardFromTrade()
    @Test
    void removeCardFromTrade_shouldCallRepository() {

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        CollectionService collectionService = new CollectionService(collectionRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Kalder metoden
        collectionService.removeCardFromTrade(user, card);

        // Tjekker at repository blev kaldt med null
        verify(collectionRepository, times(1)).updateTradeId(user, card, null);
    }
}