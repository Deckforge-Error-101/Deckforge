package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.Collection;
import org.example.deckforge.Domain.User;

import java.util.List;

public interface ICollectionRepository {
    void createCollection(int userId);
    void deleteCardFromCollection(User user, Card card);
    void addCardToCollection(User user, Card card, Collection collection);
    List<Card> findUserCollection(User user);
    int getQuantityOwned(User user, Card card);
    void updateTradeId(User user, Card card, String tradeId);
    void addCardToCollection(int userId, int cardId);
    void removeCardFromCollection(int userId, int cardId);
    CardToTrade findCardToTrade(String tradeId);
    List<CardToTrade> findAllAvailableTrades();
}
