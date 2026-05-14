package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Collection;
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

    public void addCardToCollection(int userId, int cardId, String tradeId) {
        if (userId <= 0 || cardId <= 0) {
            throw new RuntimeException("Ugyldigt ID");
        }
        iCollectionRepository.addCardToCollection(userId, cardId, tradeId);
    }

    public void removeCardFromCollection(int userId, int cardId) {
        iCollectionRepository.deleteCardFromCollection(userId, cardId);
    }

    public List<Card> getUserCollection(int userId) {
        return iCollectionRepository.findUserCollection(userId);
    }

    public List<Card> findUserCollection(int userId) {
        return iCollectionRepository.findUserCollection(userId);
    }
}