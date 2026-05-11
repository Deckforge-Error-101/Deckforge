package org.example.deckforge.Service;

import org.example.deckforge.Domain.Collection;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {

    private final ICollectionRepository iCollectionRepository;

    public CollectionService(ICollectionRepository iCollectionRepository) {
        this.iCollectionRepository = iCollectionRepository;
    }

    public void createCollection(Collection collection) {
        if (collection == null) {
            throw new RuntimeException("Collectionen kan ikke være null");
        }
        if (collection.getUserId() <= 0) {
            throw new RuntimeException("Bruger ID skal være større end 0");
        }
        if (collection.getCardId() <= 0) {
            throw new RuntimeException("Kort ID skal være større end 0");
        }
        if (collection.getTradeId() == null || collection.getTradeId().isEmpty()) {
            throw new RuntimeException("Trade ID er påkrævet");
        }

        iCollectionRepository.createCollection(collection);
    }

    public void deleteCollection(Collection collection) {
        if (collection == null || collection.getUserId() <= 0) {
            throw new RuntimeException("Ugyldig bruger ID til sletning");
        }

        iCollectionRepository.deleteCollection(collection);
    }

    public void updateCollection(Collection collection) {
        if (collection == null || collection.getCollectionId() <= 0) {
            throw new RuntimeException("Ugyldig Collection ID til opdatering");
        }
        if (collection.getCardId() <= 0) {
            throw new RuntimeException("Kort ID skal være større end 0");
        }
        if (collection.getTradeId() == null || collection.getTradeId().isEmpty()) {
            throw new RuntimeException("Trade ID er påkrævet");
        }

        iCollectionRepository.updateCollection(collection);
    }
}