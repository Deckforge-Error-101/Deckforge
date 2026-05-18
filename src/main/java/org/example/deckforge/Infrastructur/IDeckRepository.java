package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;

import java.util.List;

public interface IDeckRepository {
     Deck createDeck(Deck deck);
     List<Deck> findAllDecksByUserId(int userId);
     void updateDeck(Deck deck);
     void deleteDeck(int deckId);
     Deck findDeckById(int deckId);
     List<Card> getCardsInDeck(int deckId);
     void addCardToDeck(int deckId, int cardId);
     void removeCardFromDeck(int deckId, int cardId);
     void updateDeckVisibility(int deckId, boolean isPublic);
     List<Deck> findAllPublicDecks();
     int getQuantityInDeck(int deckId, int cardId);
}
