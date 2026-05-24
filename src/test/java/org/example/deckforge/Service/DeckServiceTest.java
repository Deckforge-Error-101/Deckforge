package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.IDeckRepository;
import org.example.deckforge.Service.Validation.DeckException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeckServiceTest {

    // Test af createDeck()
    @Test
    void createDeck_shouldCallRepository() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock deck
        Deck deck = mock(Deck.class);

        // Kalder metoden
        deckService.createDeck(deck);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).createDeck(deck);
    }

    // Test af createDeck() ved databasefejl
    @Test
    void createDeck_shouldThrowDeckException_whenRepositoryFails() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock deck
        Deck deck = mock(Deck.class);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(deckRepository).createDeck(deck);

        // Tjekker at DeckException bliver kastet
        DeckException exception = assertThrows(DeckException.class, () -> {
            deckService.createDeck(deck);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved oprettelse af deck, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).createDeck(deck);
    }

    // Test af findAllDecksByUserId()
    @Test
    void findAllDecksByUserId_shouldReturnDecks() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer gyldigt userId
        when(user.getUserId()).thenReturn(1);

        // Laver fake liste med 2 decks
        List<Deck> decks = List.of(mock(Deck.class), mock(Deck.class));

        // Bestemmer hvad repository skal returnere
        when(deckRepository.findAllDecksByUserId(user)).thenReturn(decks);

        // Kalder metoden
        List<Deck> result = deckService.findAllDecksByUserId(user);

        // Tjekker at listen indeholder 2 decks
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).findAllDecksByUserId(user);
    }

    // Test af findAllDecksByUserId() med ugyldigt userId
    @Test
    void findAllDecksByUserId_shouldThrowException_whenUserIdIsInvalid() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer ugyldigt userId
        when(user.getUserId()).thenReturn(0);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deckService.findAllDecksByUserId(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(deckRepository, never()).findAllDecksByUserId(any());
    }

    // Test af findAllDecksByUserId() ved databasefejl
    @Test
    void findAllDecksByUserId_shouldThrowDeckException_whenRepositoryFails() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer gyldigt userId
        when(user.getUserId()).thenReturn(1);

        // Simulerer databasefejl
        when(deckRepository.findAllDecksByUserId(user)).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at DeckException bliver kastet
        DeckException exception = assertThrows(DeckException.class, () -> {
            deckService.findAllDecksByUserId(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er sket en fejl ved decks, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).findAllDecksByUserId(user);
    }

    // Test af updateDeck()
    @Test
    void updateDeck_shouldCallRepository() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock decks
        Deck updatedDeck = mock(Deck.class);
        Deck existingDeck = mock(Deck.class);

        // Simulerer data fra updatedDeck
        when(updatedDeck.getDeckName()).thenReturn("Nyt deck");
        when(updatedDeck.getFormatType()).thenReturn("Commander");
        when(updatedDeck.isPublic()).thenReturn(true);

        // Bestemmer hvad repository skal returnere
        when(deckRepository.findDeckById(updatedDeck)).thenReturn(existingDeck);

        // Kalder metoden
        deckService.updateDeck(updatedDeck);

        // Tjekker at existingDeck bliver opdateret
        verify(existingDeck, times(1)).setDeckName("Nyt deck");
        verify(existingDeck, times(1)).setFormatType("Commander");
        verify(existingDeck, times(1)).setPublic(true);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).updateDeck(existingDeck);
    }

    // Test af updateDeck() med null deck
    @Test
    void updateDeck_shouldThrowException_whenDeckIsNull() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deckService.updateDeck(null);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(deckRepository, never()).updateDeck(any());
    }

    // Test af deleteDeck()
    @Test
    void deleteDeck_shouldCallRepository() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock deck
        Deck deck = mock(Deck.class);

        // Kalder metoden
        deckService.deleteDeck(deck);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).deleteDeck(deck);
    }

    // Test af deleteDeck() ved databasefejl
    @Test
    void deleteDeck_shouldThrowDeckException_whenRepositoryFails() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock deck
        Deck deck = mock(Deck.class);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(deckRepository).deleteDeck(deck);

        // Tjekker at DeckException bliver kastet
        DeckException exception = assertThrows(DeckException.class, () -> {
            deckService.deleteDeck(deck);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved sletning af deck, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).deleteDeck(deck);
    }

    // Test af findDeckById()
    @Test
    void findDeckById_shouldReturnDeck() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock decks
        Deck deck = mock(Deck.class);
        Deck foundDeck = mock(Deck.class);

        // Bestemmer hvad repository skal returnere
        when(deckRepository.findDeckById(deck)).thenReturn(foundDeck);

        // Kalder metoden
        Deck result = deckService.findDeckById(deck);

        // Tjekker at det rigtige deck bliver returneret
        assertEquals(foundDeck, result);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).findDeckById(deck);
    }

    // Test af getCardsInDeck()
    @Test
    void getCardsInDeck_shouldReturnCards() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock deck
        Deck deck = mock(Deck.class);

        // Laver fake liste med 2 kort
        List<Card> cards = List.of(mock(Card.class), mock(Card.class));

        // Bestemmer hvad repository skal returnere
        when(deckRepository.getCardsInDeck(deck)).thenReturn(cards);

        // Kalder metoden
        List<Card> result = deckService.getCardsInDeck(deck);

        // Tjekker at listen indeholder 2 kort
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).getCardsInDeck(deck);
    }

    // Test af addCardToDeck() når brugeren ejer flere kort
    @Test
    void addCardToDeck_shouldAddCard_whenUserOwnsMoreThanInDeck() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock objekter
        User user = mock(User.class);
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        // Simulerer at brugeren ejer 2 kopier
        when(collectionRepository.getQuantityOwned(user, card)).thenReturn(2);

        // Simulerer at der allerede er 1 kopi i decket
        when(deckRepository.getQuantityInDeck(deck, card)).thenReturn(1);

        // Kalder metoden
        deckService.addCardToDeck(user, deck, card);

        // Tjekker at kortet blev tilføjet
        verify(deckRepository, times(1)).addCardToDeck(deck, card);
    }

    // Test af addCardToDeck() når brugeren ikke ejer flere kopier
    @Test
    void addCardToDeck_shouldThrowException_whenUserHasNoMoreCopies() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock objekter
        User user = mock(User.class);
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        // Simulerer at brugeren ejer 1 kopi
        when(collectionRepository.getQuantityOwned(user, card)).thenReturn(1);

        // Simulerer at decket allerede har 1 kopi
        when(deckRepository.getQuantityInDeck(deck, card)).thenReturn(1);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deckService.addCardToDeck(user, deck, card);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at kortet IKKE blev tilføjet
        verify(deckRepository, never()).addCardToDeck(any(), any());
    }

    // Test af removeCardFromDeck()
    @Test
    void removeCardFromDeck_shouldCallRepository() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock objekter
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        // Kalder metoden
        deckService.removeCardFromDeck(deck, card);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).removeCardFromDeck(deck, card);
    }

    // Test af updateDeckVisibility()
    @Test
    void updateDeckVisibility_shouldCallRepository() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock deck
        Deck deck = mock(Deck.class);

        // Kalder metoden
        deckService.updateDeckVisibility(deck);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).updateDeckVisibility(deck);
    }

    // Test af findAllPublicDecks()
    @Test
    void findAllPublicDecks_shouldReturnDecksWithCards() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake/mock decks
        Deck deck1 = mock(Deck.class);
        Deck deck2 = mock(Deck.class);

        // Laver fake liste med public decks
        List<Deck> decks = List.of(deck1, deck2);

        // Laver fake liste med kort
        List<Card> cards = List.of(mock(Card.class), mock(Card.class));

        // Bestemmer hvad repository skal returnere
        when(deckRepository.findAllPublicDecks()).thenReturn(decks);
        when(deckRepository.getCardsInDeck(deck1)).thenReturn(cards);
        when(deckRepository.getCardsInDeck(deck2)).thenReturn(cards);

        // Kalder metoden
        List<Deck> result = deckService.findAllPublicDecks();

        // Tjekker at listen indeholder 2 decks
        assertEquals(2, result.size());

        // Tjekker at kort bliver sat på hvert deck
        verify(deck1, times(1)).setCards(cards);
        verify(deck2, times(1)).setCards(cards);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).findAllPublicDecks();
        verify(deckRepository, times(1)).getCardsInDeck(deck1);
        verify(deckRepository, times(1)).getCardsInDeck(deck2);
    }

    // Test af findAllPublicDecks() ved databasefejl
    @Test
    void findAllPublicDecks_shouldThrowDeckException_whenRepositoryFails() {

        // Laver fake/mock repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Simulerer databasefejl
        when(deckRepository.findAllPublicDecks()).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at DeckException bliver kastet
        DeckException exception = assertThrows(DeckException.class, () -> {
            deckService.findAllPublicDecks();
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er sket en fejl ved fjernelse af kort, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).findAllPublicDecks();
    }
}