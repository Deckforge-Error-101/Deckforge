package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.Collection;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Service.Validation.CollectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    private final ICollectionRepository iCollectionRepository;

    public CollectionService(ICollectionRepository iCollectionRepository) {
        this.iCollectionRepository = iCollectionRepository;
    }

    public void initializeEmptyCollection(int userId) {
        try {
            iCollectionRepository.createCollection(userId);
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fejl ved indlæsning af collection, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public void addCardToCollection(User user, Card card, Collection collection) {
        try {
            if (user.getUserId() <= 0 || card.getCardId() <= 0) {
                throw new RuntimeException("Ugyldigt ID");
            }
            iCollectionRepository.addCardToCollection(user, card, collection);
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fejl ved tilføjelse af kort, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public void removeCardFromCollection(User user, Card card) {
        try {
            iCollectionRepository.deleteCardFromCollection(user, card);
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er sket en fejl ved fjernelse af kort, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public List<Card> getUserCollection(User user) {
        try {
            return iCollectionRepository.findUserCollection(user);
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fej ved collection, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public List<Card> findUserCollection(User user) {
        try {
            return iCollectionRepository.findUserCollection(user);
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fejl ved collection, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public List<CardToTrade> getAllAvailableTrades() {
        try {
            return iCollectionRepository.findAllAvailableTrades();
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fejl ved collection prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public void setCardAsTradeable(User user, Card card) {
        try {
            if (user.getUserId() <= 0 || card.getCardId() <= 0) {
                throw new RuntimeException("Ugyldigt ID");
            }
            iCollectionRepository.updateTradeId(user, card, "ACTIVE");
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fejl ved tilføjelse af kort til bytning, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public void removeCardFromTrade(User user, Card card) {
        try {
            iCollectionRepository.updateTradeId(user, card, null);
        } catch (DataAccessException dae) {
            throw new CollectionException("Der er skete en fejl ved fjernelse af kort fra bytning, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }
}