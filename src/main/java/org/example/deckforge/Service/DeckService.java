package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;
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

    public void createDeck(Deck deck) {
        iDeckRepository.createDeck(deck);
    }

    public List<Deck> findAllDecksByUserId(User user) {
        if (user.getUserId() <= 0) {
            throw new RuntimeException("Ugyldigt bruger ID");
        }

        return iDeckRepository.findAllDecksByUserId(user);
    }

    public void updateDeck(Deck updatedDeck) {
        if (updatedDeck == null) {
            throw new RuntimeException("Dækket må ikke være null");
        }

        Deck existingDeck = iDeckRepository.findDeckById(updatedDeck);

        existingDeck.setDeckName(updatedDeck.getDeckName());
        existingDeck.setFormatType(updatedDeck.getFormatType());
        existingDeck.setPublic(updatedDeck.isPublic());

        iDeckRepository.updateDeck(existingDeck);
    }

    public void deleteDeck(Deck deck) {
        iDeckRepository.deleteDeck(deck);
    }

    public Deck findDeckById(Deck deck) {
        return iDeckRepository.findDeckById(deck);
    }

    public List<Card> getCardsInDeck(Deck deck) {
        return iDeckRepository.getCardsInDeck(deck);
    }

    public void addCardToDeck(User user, Deck deck, Card card){
        int owned = iCollectionRepository.getQuantityOwned(user, card);
        int inDeck = iDeckRepository.getQuantityInDeck(deck, card);

        if (inDeck < owned){
            iDeckRepository.addCardToDeck(deck, card);
        } else {
            throw new IllegalStateException("Du har ikke flere eksemplarer af dette kort i din collection");
        }
    }

    public void removeCardFromDeck(Deck deck, Card card){
        iDeckRepository.removeCardFromDeck(deck, card);
    }

    public void updateDeckVisibility(Deck deck){
        iDeckRepository.updateDeckVisibility(deck);
    }

    public List<Deck> findAllPublicDecks(){
        List<Deck> decks = iDeckRepository.findAllPublicDecks();

        for (Deck deck: decks){
            List<Card> cardsInThisDeck = iDeckRepository.getCardsInDeck(deck);

            deck.setCards(cardsInThisDeck);
        }
        return decks;
    }
}
