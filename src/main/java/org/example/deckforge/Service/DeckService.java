package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.IDeckRepository;
import org.example.deckforge.Service.Validation.DeckException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final IDeckRepository iDeckRepository;
    private final ICollectionRepository iCollectionRepository;

    public DeckService(IDeckRepository iDeckRepository, ICollectionRepository iCollectionRepository) {
        this.iDeckRepository = iDeckRepository;
        this.iCollectionRepository = iCollectionRepository;
    }

    public void createDeck(Deck deck) {
        try {
            iDeckRepository.createDeck(deck);
        } catch (DataAccessException dae) {
            throw new DeckException("Fejl ved oprettelse af deck, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public List<Deck> findAllDecksByUserId(User user) {
        try {
            if (user.getUserId() <= 0) {
                throw new RuntimeException("Ugyldigt bruger ID");
            }

            return iDeckRepository.findAllDecksByUserId(user);
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved decks, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void updateDeck(Deck updatedDeck) {
        try {
            if (updatedDeck == null) {
                throw new RuntimeException("Dækket må ikke være null");
            }

            Deck existingDeck = iDeckRepository.findDeckById(updatedDeck);

            existingDeck.setDeckName(updatedDeck.getDeckName());
            existingDeck.setFormatType(updatedDeck.getFormatType());
            existingDeck.setPublic(updatedDeck.isPublic());

            iDeckRepository.updateDeck(existingDeck);
        } catch (DataAccessException dae) {
            throw new DeckException("Fejl ved opdatering af deck, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void deleteDeck(Deck deck) {
        try {
            iDeckRepository.deleteDeck(deck);
        } catch (DataAccessException dae) {
            throw new DeckException("Fejl ved sletning af deck, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public Deck findDeckById(Deck deck) {
        try {
            return iDeckRepository.findDeckById(deck);
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved decks, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public List<Card> getCardsInDeck(Deck deck) {
        try {
            return iDeckRepository.getCardsInDeck(deck);
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved decks, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void addCardToDeck(User user, Deck deck, Card card) {
        try {
            int owned = iCollectionRepository.getQuantityOwned(user, card);
            int inDeck = iDeckRepository.getQuantityInDeck(deck, card);

            if (inDeck < owned) {
                iDeckRepository.addCardToDeck(deck, card);
            } else {
                throw new IllegalStateException("Du har ikke flere eksemplarer af dette kort i din collection");
            }
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved tilføjelse af kort, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void removeCardFromDeck(Deck deck, Card card) {
        try {
            iDeckRepository.removeCardFromDeck(deck, card);
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved fjernelse af kort, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public void updateDeckVisibility(Deck deck) {
        try {
            iDeckRepository.updateDeckVisibility(deck);
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved dit decks synlighed, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public List<Deck> findAllPublicDecks() {
        try {
            List<Deck> decks = iDeckRepository.findAllPublicDecks();

            for (Deck deck : decks) {
                List<Card> cardsInThisDeck = iDeckRepository.getCardsInDeck(deck);

                deck.setCards(cardsInThisDeck);
            }
            return decks;
        } catch (DataAccessException dae) {
            throw new DeckException("Der er sket en fejl ved fjernelse af kort, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }
}
