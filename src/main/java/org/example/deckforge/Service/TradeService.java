package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.ITradeLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TradeService {
    private ICollectionRepository iCollectionRepository;
    private ITradeLogRepository iTradeLogRepository;

    public TradeService(ICollectionRepository iCollectionRepository) {
        this.iCollectionRepository = iCollectionRepository;
        this.iTradeLogRepository = iTradeLogRepository;
    }

    public String generateTradeCode(User user, Card card){
        String tradeCode = UUID.randomUUID().toString().substring(0, 8);
        iCollectionRepository.updateTradeId(user, card, tradeCode);
        return tradeCode;
    }

    @Transactional
    public String redeemCard(String tradeId, int buyerId){
        CardToTrade trade = iCollectionRepository.findCardToTrade(tradeId);

        if (trade == null){
            throw new IllegalArgumentException("Koden er enten ugyldig eller allerede brugt");
        }

        if (trade.getOwnerId() == buyerId){
            throw new IllegalArgumentException("Du kan ikke indløse din egen kode");
        }

        iCollectionRepository.removeCardFromCollection(trade.getOwnerId(), trade.getCardId());
        iCollectionRepository.addCardToCollection(buyerId, trade.getCardId());
        return trade.getCardName();
    }
}
