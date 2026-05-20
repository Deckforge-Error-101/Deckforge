/*
package org.example.deckforge.Service;


import org.example.deckforge.Domain.Card;
import org.example.deckforge.Infrastructur.ICardRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    // Test af createCard()
    @Test
    void createCard_shouldCreateCard() throws Exception {

        // Laver fake/mock repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter CardService med mock repository
        CardService cardService = new CardService(cardRepository);

        // Opretter nyt Card objekt
        Card card = new Card();

        // Sætter navn på kortet
        card.setCardName("Blue Eyes White Dragon");

        // Kalder metoden vi vil teste
        cardService.createCard(card);

        // Tjekker at repository.createCard()
        // blev kaldt præcis 1 gang
        verify(cardRepository, times(1)).createCard(card);
    }

    // Test af createCard() med ugyldigt kort
    @Test
    void createCard_shouldThrowException_whenCardNameIsNull() {

        // Laver fake repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Opretter kort uden navn
        Card card = new Card();

        // Tjekker at metoden kaster exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {cardService.createCard(card);});

        // Tjekker fejlbeskeden
        assertEquals("Kortet skal have et navn", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(cardRepository, never()).createCard(any(Card.class));
    }

    // Test af findById()
    @Test
    void findById_shouldReturnCard() throws Exception {

        // Laver fake repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Opretter test kort
        Card card = new Card();

        card.setCardId(1);
        card.setCardName("Dark Magician");

        // Bestemmer hvad repository skal returnere
        when(cardRepository.findById(1)).thenReturn(card);

        // Kalder metoden
        Card result = cardService.findById(1);

        // Tjekker at resultatet ikke er null
        assertNotNull(result);

        // Tjekker at navnet passer
        assertEquals("Dark Magician", result.getCardName());
    }

    // Test af negativt cardId
    @Test
    void findById_shouldThrowException_whenIdIsNegative() {

        // Laver fake repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {cardService.findById(-1);});

        // Tjekker fejlbesked
        assertEquals("Card id kan ikke være negativ", exception.getMessage());
    }

    // Test hvis kort ikke findes
    @Test
    void findById_shouldThrowException_whenCardDoesNotExist() {

        // Laver fake repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Repository returnerer null
        when(cardRepository.findById(1)).thenReturn(null);

        // Tjekker exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {cardService.findById(1);});

        // Tjekker fejlbesked
        assertEquals("Kortet blev ikke fundet", exception.getMessage());
    }

    // Test af findAllCards()
    @Test
    void findAllCards_shouldReturnList() {

        // Laver fake repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Laver testliste med kort
        List<Card> cards = List.of(new Card(), new Card());

        // Bestemmer hvad repository returnerer
        when(cardRepository.findAllCards()).thenReturn(cards);

        // Kalder metoden
        List<Card> result = cardService.findAllCards();

        // Tjekker at listen har 2 elementer
        assertEquals(2, result.size());
    }
}

 */