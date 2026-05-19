package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;

import java.util.List;

public interface IDeckRepository {
     void createDeck(Deck deck);
     List<Deck> findAllDecksByUserId(User user);
     void updateDeck(Deck deck);
     void deleteDeck(Deck deck);
     Deck findDeckById(Deck deck);
     List<Card> getCardsInDeck(Deck deck);
     void addCardToDeck(Deck deck, Card card);
     void removeCardFromDeck(Deck deck, Card card);
     void updateDeckVisibility(Deck deck);
     List<Deck> findAllPublicDecks();
     int getQuantityInDeck(Deck deck, Card card);
}
