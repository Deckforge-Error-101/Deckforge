package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Infrastructur.IDeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final IDeckRepository iDeckRepository;

    public DeckService(IDeckRepository iDeckRepository) {
        this.iDeckRepository = iDeckRepository;
    }

    public void createDeck(Deck deck) {
        iDeckRepository.createDeck(deck);
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

    public void deleteDeck(Deck deck) {
        iDeckRepository.deleteDeck(deck);
    }

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

    public Deck findDeckById(int deckId) {
        return iDeckRepository.findDeckById(deckId);
    }

    public List<Card> getCardsInDeck(int deckId) {
        return iDeckRepository.getCardsInDeck(deckId);
    }

    public void addCardToDeck(int deckId, int cardId){
        iDeckRepository.addCardToDeck(deckId, cardId);
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
