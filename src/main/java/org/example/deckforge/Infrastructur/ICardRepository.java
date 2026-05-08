package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;

import java.util.List;

public interface ICardRepository {
    void createCard(Card card);
    Card findById(int cardId);
    List<Card> findAllCards();
}
