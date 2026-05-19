package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.Collection;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.CollectionRepository;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    private final ICollectionRepository iCollectionRepository;

    public CollectionService(ICollectionRepository iCollectionRepository) {
        this.iCollectionRepository = iCollectionRepository;
    }

    public void initializeEmptyCollection(int userId) {
        iCollectionRepository.createCollection(userId);
    }

    public void addCardToCollection(User user, Card card, Collection collection) {
        if (user.getUserId() <= 0 || card.getCardId() <= 0) {
            throw new RuntimeException("Ugyldigt ID");
        }
        iCollectionRepository.addCardToCollection(user, card, collection);
    }

    public void removeCardFromCollection(User user, Card card) {
        iCollectionRepository.deleteCardFromCollection(user, card);
    }

    public List<Card> getUserCollection(User user) {
        return iCollectionRepository.findUserCollection(user);
    }

    public List<Card> findUserCollection(User user) {
        return iCollectionRepository.findUserCollection(user);
    }

    public List<CardToTrade> getAllAvailableTrades() {
        return iCollectionRepository.findAllAvailableTrades();
    }

    public void setCardAsTradeable(User user, Card card) {
        if (user.getUserId() <= 0 || card.getCardId() <= 0) {
            throw new RuntimeException("Ugyldigt ID");
        }
        iCollectionRepository.updateTradeId(user, card, "ACTIVE");
    }

    public void removeCardFromTrade(User user, Card card) {
        iCollectionRepository.updateTradeId(user, card, null);
    }
}