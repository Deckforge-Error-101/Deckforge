package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Infrastructur.ICardRepository;
import org.example.deckforge.Service.Validation.CardException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final ICardRepository iCardRepository;

    public CardService(ICardRepository iCardRepository) {
        this.iCardRepository = iCardRepository;
    }

    public void createCard(Card card) {
        try {
            if (card == null || card.getCardName() == null || card.getCardName().isEmpty()) {
                throw new RuntimeException("Kortet skal have et navn");
            }
            iCardRepository.createCard(card);
        } catch (DataAccessException dae) {
            throw new CardException("Der er sket en fejl ved oprettelse af kort, prøv igen senere" + dae.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }

    public List<Card> findAllCards() {
        try {
            return iCardRepository.findAllCards();
        } catch (DataAccessException dae) {
            throw new CardException("Der er sket en fejl ved at finde kort, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt en administrator");
        }
    }
}
