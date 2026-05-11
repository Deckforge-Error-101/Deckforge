package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Infrastructur.ICardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final ICardRepository iCardRepository;

    public CardService(ICardRepository iCardRepository) {
        this.iCardRepository = iCardRepository;
    }

    public void createCard(Card card) {
        if (card == null || card.getCardName() == null || card.getCardName().isEmpty()) {
            throw new RuntimeException("Kortet skal have et navn");
        }
        iCardRepository.createCard(card);
    }

    public Card findById(int cardId) {
        if (cardId < 0) {
            throw new RuntimeException("Card id kan ikke være negativ");
        }

        Card card = iCardRepository.findById(cardId);
        if (card == null) {
            throw new RuntimeException("Kortet blev ikke fundet");
        }

        return card;
    }

    public List<Card> findAllCards() {
        return iCardRepository.findAllCards();
    }


}
