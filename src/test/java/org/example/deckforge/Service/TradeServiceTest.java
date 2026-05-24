package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICardRepository;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.ITradeLogRepository;
import org.example.deckforge.Infrastructur.IUserRepository;
import org.example.deckforge.Service.Validation.TradeException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataAccessResourceFailureException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TradeServiceTest {

    // Test af generateTradeCode()
    @Test
    void generateTradeCode_shouldReturnTradeCodeAndUpdateRepository() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Kalder metoden
        String tradeCode = tradeService.generateTradeCode(user, card);

        // Tjekker at tradeCode ikke er null
        assertNotNull(tradeCode);

        // Tjekker at tradeCode har 8 tegn
        assertEquals(8, tradeCode.length());

        // Tjekker at repository blev kaldt med samme kode
        verify(collectionRepository, times(1)).updateTradeId(user, card, tradeCode);
    }

    // Test af generateTradeCode() ved databasefejl
    @Test
    void generateTradeCode_shouldThrowTradeException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock user og card
        User user = mock(User.class);
        Card card = mock(Card.class);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(collectionRepository).updateTradeId(eq(user), eq(card), anyString());

        // Tjekker at TradeException bliver kastet
        TradeException exception = assertThrows(TradeException.class, () -> {
            tradeService.generateTradeCode(user, card);
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er sket en fejl ved trade koden, prøv igen senere", exception.getMessage());
    }

    // Test af redeemCard()
    @Test
    void redeemCard_shouldMoveCardAndInsertLog_whenTradeIsValid() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock objekter
        CardToTrade trade = mock(CardToTrade.class);
        User buyer = mock(User.class);
        User seller = mock(User.class);
        Card card = mock(Card.class);

        // Simulerer data
        when(buyer.getUserId()).thenReturn(2);
        when(trade.getOwnerId()).thenReturn(1);
        when(trade.getCardId()).thenReturn(10);
        when(trade.getCardName()).thenReturn("Charizard");

        // Bestemmer hvad repositories skal returnere
        when(collectionRepository.findCardToTrade("ABC12345")).thenReturn(trade);
        when(userRepository.findUser(any(User.class))).thenReturn(seller);
        when(cardRepository.findById(10)).thenReturn(card);

        // Kalder metoden
        String result = tradeService.redeemCard("ABC12345", buyer);

        // Tjekker at kortnavnet bliver returneret
        assertEquals("Charizard", result);

        // Tjekker at kortet bliver fjernet fra seller
        verify(collectionRepository, times(1)).removeCardFromCollection(1, 10);

        // Tjekker at kortet bliver tilføjet til buyer
        verify(collectionRepository, times(1)).addCardToCollection(2, 10);

        // Tjekker at trade log bliver oprettet
        verify(tradeLogRepository, times(1)).insertLog(buyer, seller, card);
    }

    // Test af redeemCard() med ugyldig kode
    @Test
    void redeemCard_shouldThrowException_whenTradeCodeIsInvalid() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock buyer
        User buyer = mock(User.class);

        // Simulerer at trade ikke findes
        when(collectionRepository.findCardToTrade("FORKERT")).thenReturn(null);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tradeService.redeemCard("FORKERT", buyer);
        });

        // Tjekker fejlbeskeden
        assertEquals("Koden er enten ugyldig eller allerede brugt", exception.getMessage());

        // Tjekker at kort ikke bliver flyttet
        verify(collectionRepository, never()).removeCardFromCollection(anyInt(), anyInt());
        verify(collectionRepository, never()).addCardToCollection(anyInt(), anyInt());
    }

    // Test af redeemCard() når buyer ejer koden selv
    @Test
    void redeemCard_shouldThrowException_whenBuyerOwnsTradeCode() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock objekter
        CardToTrade trade = mock(CardToTrade.class);
        User buyer = mock(User.class);

        // Simulerer at buyer og owner er samme bruger
        when(buyer.getUserId()).thenReturn(1);
        when(trade.getOwnerId()).thenReturn(1);

        // Bestemmer hvad repository skal returnere
        when(collectionRepository.findCardToTrade("ABC12345")).thenReturn(trade);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tradeService.redeemCard("ABC12345", buyer);
        });

        // Tjekker fejlbeskeden
        assertEquals("Du kan ikke indløse din egen kode", exception.getMessage());

        // Tjekker at kort ikke bliver flyttet
        verify(collectionRepository, never()).removeCardFromCollection(anyInt(), anyInt());
        verify(collectionRepository, never()).addCardToCollection(anyInt(), anyInt());
    }

    // Test af redeemCard() ved databasefejl
    @Test
    void redeemCard_shouldThrowTradeException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock buyer
        User buyer = mock(User.class);

        // Simulerer databasefejl
        when(collectionRepository.findCardToTrade("ABC12345")).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at TradeException bliver kastet
        TradeException exception = assertThrows(TradeException.class, () -> {
            tradeService.redeemCard("ABC12345", buyer);
        });

        // Tjekker fejlbeskeden
        assertEquals("Der er sket en fejl ved trade, prøv igen senere", exception.getMessage());
    }

    // Test af redeemCard() uden seller eller card
    @Test
    void redeemCard_shouldMoveCardWithoutLog_whenSellerOrCardIsNull() throws Exception {

        // Laver fake/mock repositories
        ICollectionRepository collectionRepository = mock(ICollectionRepository.class);
        ITradeLogRepository tradeLogRepository = mock(ITradeLogRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICardRepository cardRepository = mock(ICardRepository.class);

        // Opretter service
        TradeService tradeService = new TradeService(collectionRepository, tradeLogRepository, userRepository, cardRepository);

        // Laver fake/mock objekter
        CardToTrade trade = mock(CardToTrade.class);
        User buyer = mock(User.class);

        // Simulerer data
        when(buyer.getUserId()).thenReturn(2);
        when(trade.getOwnerId()).thenReturn(1);
        when(trade.getCardId()).thenReturn(10);
        when(trade.getCardName()).thenReturn("Pikachu");

        // Bestemmer hvad repositories skal returnere
        when(collectionRepository.findCardToTrade("ABC12345")).thenReturn(trade);
        when(userRepository.findUser(any(User.class))).thenReturn(null);
        when(cardRepository.findById(10)).thenReturn(null);

        // Kalder metoden
        String result = tradeService.redeemCard("ABC12345", buyer);

        // Tjekker at kortnavnet bliver returneret
        assertEquals("Pikachu", result);

        // Tjekker at kortet stadig bliver flyttet
        verify(collectionRepository, times(1)).removeCardFromCollection(1, 10);
        verify(collectionRepository, times(1)).addCardToCollection(2, 10);

        // Tjekker at trade log IKKE bliver oprettet
        verify(tradeLogRepository, never()).insertLog(any(), any(), any());
    }
}