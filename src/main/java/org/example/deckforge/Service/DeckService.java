package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.IDeckRepository;
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

    public Deck createDeck(Deck deck) {
        return iDeckRepository.createDeck(deck);
    }

    public List<Deck> findAllDecksByUserId(int userId) {
        if (userId <= 0) {
            throw new RuntimeException("Ugyldigt bruger ID");
        }

        return iDeckRepository.findAllDecksByUserId(userId);
    }

    public void updateDeck(Deck deck) {
        iDeckRepository.updateDeck(deck);
    }

    public void deleteDeck(int deckId) {
        iDeckRepository.deleteDeck(deckId);
    }

    /* Vi skal validere i en validate klasse. Gemmes så vi kan copy over i den korrekte klasse.
    private void validateDeckRules(Deck deck) {
        if (deck.getDeckName() == null || deck.getDeckName().isEmpty()) {
            throw new RuntimeException("Decket skal have et navn");
        }

        String format = deck.getFormatType();
        int slots = deck.getSlots();

        if (format != null && format.equalsIgnoreCase("commander")) {
            if (slots < 100) {
                throw new RuntimeException("Et Commander deck skal have mindst 100 kort");
            }
        } else {
            if (slots < 60) {
                throw new RuntimeException("Et deck skal have mindst 60 kort");
            }
        }
    }

     */

    public Deck findDeckById(int deckId) {
        return iDeckRepository.findDeckById(deckId);
    }

    public List<Card> getCardsInDeck(int deckId) {
        return iDeckRepository.getCardsInDeck(deckId);
    }

    public void addCardToDeck(int userId, int deckId, int cardId){
        int owned = iCollectionRepository.getQuantityOwned(userId, cardId);
        int inDeck = iDeckRepository.getQuantityInDeck(deckId, cardId);

        if (inDeck < owned){
            iDeckRepository.addCardToDeck(deckId, cardId);
        } else {
            throw new IllegalStateException("Du har ikke flere eksemplarer af dette kort i din collection");
        }
    }

    public void removeCardFromDeck(int deckId, int cardId){
        iDeckRepository.removeCardFromDeck(deckId, cardId);
    }

    public void updateDeckVisibility(int deckId, boolean isPublic){
        iDeckRepository.updateDeckVisibility(deckId, isPublic);
    }

    public List<Deck> findAllPublicDecks(){
        List<Deck> decks = iDeckRepository.findAllPublicDecks();

        for (Deck deck: decks){
            List<Card> cardsInThisDeck = iDeckRepository.getCardsInDeck(deck.getDeckId());

            deck.setCards(cardsInThisDeck);
        }
        return decks;
    }
}
