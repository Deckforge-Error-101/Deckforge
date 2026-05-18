package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.Collection;

import java.util.List;

public interface ICollectionRepository {
    void createCollection(int userId);
    void deleteCardFromCollection(int userId, int cardId);
    void addCardToCollection(int userId, int cardId, String tradeId);
    List<Card> findUserCollection(int userId);
    int getQuantityOwned(int userId, int cardId);
    void updateTradeId(int userId, int cardId, String tradeId);
    void addCardToCollection(int userId, int cardId);
    void removeCardFromCollection(int userId, int cardId);
    CardToTrade findCardToTrade(String tradeId);
}
