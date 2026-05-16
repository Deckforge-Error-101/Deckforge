package org.example.deckforge.Service;

import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.IDeckRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeckServiceTest {

    // Test af createDeck()
    @Test
    void createDeck_shouldCallRepository() {

        // Laver fake/mock deck repository
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        // Laver fake/mock collection repository
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter DeckService med mocks
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Opretter nyt deck objekt
        Deck deck = new Deck();

        // Kalder metoden vi vil teste
        deckService.createDeck(deck);

        // Tjekker at repository.createDeck()
        // blev kaldt præcis 1 gang
        verify(deckRepository, times(1)).createDeck(deck);
    }

    // Test af findAllDecksByUserId()
    @Test
    void findAllDecksByUserId_shouldReturnDecks() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Laver fake liste med 2 decks
        List<Deck> decks = List.of(new Deck(), new Deck());

        // Bestemmer hvad repository skal returnere
        when(deckRepository.findAllDecksByUserId(1)).thenReturn(decks);

        // Kalder metoden
        List<Deck> result = deckService.findAllDecksByUserId(1);

        // Tjekker at listen indeholder 2 decks
        assertEquals(2, result.size());

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).findAllDecksByUserId(1);
    }

    // Test af ugyldigt userId
    @Test
    void findAllDecksByUserId_shouldThrowException_whenUserIdIsInvalid() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {deckService.findAllDecksByUserId(0);});

        // Tjekker fejlbeskeden
        assertEquals("Ugyldigt bruger ID", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(deckRepository, never()).findAllDecksByUserId(anyInt());
    }

    // Test af deleteDeck()
    @Test
    void deleteDeck_shouldCallRepository() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Kalder delete metoden
        deckService.deleteDeck(3);

        // Tjekker at repository.deleteDeck()
        // blev kaldt korrekt
        verify(deckRepository, times(1)).deleteDeck(3);
    }

    // Test af addCardToDeck() når brugeren ejer kortet
    @Test
    void addCardToDeck_shouldAddCard_whenUserOwnsMoreThanInDeck() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Simulerer at brugeren ejer 2 kopier
        when(collectionRepository.getQuantityOwned(1, 10)).thenReturn(2);

        // Simulerer at der allerede er 1 kopi i decket
        when(deckRepository.getQuantityInDeck(5, 10)).thenReturn(1);

        // Kalder metoden
        deckService.addCardToDeck(1, 5, 10);

        // Tjekker at kortet blev tilføjet
        verify(deckRepository, times(1)).addCardToDeck(5, 10);
    }

    // Test af addCardToDeck() når brugeren ikke ejer flere kopier
    @Test
    void addCardToDeck_shouldThrowException_whenUserHasNoMoreCopies() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Simulerer at brugeren ejer 1 kopi
        when(collectionRepository.getQuantityOwned(1, 10)).thenReturn(1);

        // Simulerer at decket allerede har 1 kopi
        when(deckRepository.getQuantityInDeck(5, 10)).thenReturn(1);

        // Tjekker at exception bliver kastet
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {deckService.addCardToDeck(1, 5, 10);});

        // Tjekker fejlbeskeden
        assertEquals("Du har ikke flere eksemplarer af dette kort i din collection", exception.getMessage());

        // Tjekker at addCardToDeck IKKE blev kaldt
        verify(deckRepository, never()).addCardToDeck(anyInt(), anyInt());
    }

    // Test af removeCardFromDeck()
    @Test
    void removeCardFromDeck_shouldCallRepository() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Kalder metoden
        deckService.removeCardFromDeck(5, 10);

        // Tjekker at repository blev kaldt
        verify(deckRepository, times(1)).removeCardFromDeck(5, 10);
    }

    // Test af updateDeckVisibility()
    @Test
    void updateDeckVisibility_shouldCallRepository() {

        // Laver fake repositories
        IDeckRepository deckRepository = mock(IDeckRepository.class);

        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);

        // Opretter service
        DeckService deckService = new DeckService(deckRepository, collectionRepository);

        // Kalder metoden
        deckService.updateDeckVisibility(5, true);

        // Tjekker at repository blev kaldt korrekt
        verify(deckRepository, times(1)).updateDeckVisibility(5, true);
    }
}