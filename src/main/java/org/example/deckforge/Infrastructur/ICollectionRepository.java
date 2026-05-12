package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Collection;

import java.util.List;

public interface ICollectionRepository {
    void createCollection(int userId);
    void deleteCardFromCollection(int userId, int cardId);
    void addCardToCollection(int userId, int cardId, String tradeId);
    List<Card> findUserCollection(int userId);
}
