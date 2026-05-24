package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Infrastructur.ICardRepository;
import org.example.deckforge.Service.Validation.CardException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    // Test af createCard()
    @Test
    void createCard_shouldCallRepository_whenCardIsValid() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter CardService med mock repository
        CardService cardService = new CardService(cardRepository);

        // Opretter nyt card objekt
        Card card = new Card();
        card.setCardName("Charizard");

        // Kalder metoden vi vil teste
        cardService.createCard(card);

        // Tjekker at repository.createCard()
        // blev kaldt præcis 1 gang
        verify(cardRepository, times(1)).createCard(card);
    }

    // Test af createCard() med null card
    @Test
    void createCard_shouldThrowException_whenCardIsNull() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.createCard(null);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(cardRepository, never()).createCard(any());
    }

    // Test af createCard() uden cardName
    @Test
    void createCard_shouldThrowException_whenCardNameIsNull() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Laver fake/mock card objekt
        Card card = mock(Card.class);

        // Simulerer at cardName er null
        when(card.getCardName()).thenReturn(null);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.createCard(card);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(cardRepository, never()).createCard(any());
    }

    // Test af createCard() med tomt cardName
    @Test
    void createCard_shouldThrowException_whenCardNameIsEmpty() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Laver fake/mock card objekt
        Card card = mock(Card.class);

        // Simulerer at cardName er tomt
        when(card.getCardName()).thenReturn("");

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.createCard(card);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at repository IKKE blev kaldt
        verify(cardRepository, never()).createCard(any());
    }

    // Test af createCard() ved databasefejl
    @Test
    void createCard_shouldThrowCardException_whenRepositoryFails() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Opretter gyldigt card objekt
        Card card = new Card();
        card.setCardName("Pikachu");

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(cardRepository).createCard(card);

        // Tjekker at CardException bliver kastet
        CardException exception = assertThrows(CardException.class, () -> {
            cardService.createCard(card);
        });

        // Tjekker fejlbeskeden
        assertTrue(exception.getMessage().contains("Der er sket en fejl ved oprettelse af kort"));

        // Tjekker at repository blev kaldt
        verify(cardRepository, times(1)).createCard(card);
    }

    // Test af findAllCards()
    @Test
    void findAllCards_shouldReturnCards() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Opretter fake kort
        Card card1 = new Card();
        card1.setCardName("Charizard");

        Card card2 = new Card();
        card2.setCardName("Pikachu");

        // Laver fake liste med 2 kort
        List<Card> cards = List.of(card1, card2);

        // Bestemmer hvad repository skal returnere
        when(cardRepository.findAllCards()).thenReturn(cards);

        // Kalder metoden
        List<Card> result = cardService.findAllCards();

        // Tjekker at listen indeholder 2 kort
        assertEquals(2, result.size());

        // Tjekker navnene på kortene
        assertEquals("Charizard", result.get(0).getCardName());
        assertEquals("Pikachu", result.get(1).getCardName());

        // Tjekker at repository blev kaldt
        verify(cardRepository, times(1)).findAllCards();
    }

    // Test af findAllCards() ved databasefejl
    @Test
    void findAllCards_shouldThrowCardException_whenRepositoryFails() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Simulerer databasefejl
        when(cardRepository.findAllCards()).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at CardException bliver kastet
        CardException exception = assertThrows(CardException.class, () -> {
            cardService.findAllCards();
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er sket en fejl ved at finde kort, prøv igen senere", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(cardRepository, times(1)).findAllCards();
    }

    // Test af findAllCards() ved ukendt fejl
    @Test
    void findAllCards_shouldThrowRuntimeException_whenUnknownErrorHappens() {

        // Laver fake/mock card repository
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        CardService cardService = new CardService(cardRepository);

        // Simulerer ukendt fejl
        when(cardRepository.findAllCards()).thenThrow(new RuntimeException("Unexpected error"));

        // Tjekker at RuntimeException bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.findAllCards();
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt en administrator", exception.getMessage());

        // Tjekker at repository blev kaldt
        verify(cardRepository, times(1)).findAllCards();
    }
}