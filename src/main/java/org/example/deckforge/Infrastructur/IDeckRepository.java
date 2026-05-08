package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Deck;

import java.util.List;

public interface IDeckRepository {
     void createDeck(Deck deck);
     List<Deck> findAllDecksByUserId(int userId);
     void updateDeck(Deck deck);
     void deleteDeck(Deck deck);

}
