package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Collection;

public interface ICollectionRepository {
    void createCollection(int userId);
    void deleteCardFromCollection(int userId, int cardId);
    void addCardToCollection(int userId, int cardId, String tradeId);
}
