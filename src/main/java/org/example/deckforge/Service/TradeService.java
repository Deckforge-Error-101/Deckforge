package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.ICardRepository;
import org.example.deckforge.Infrastructur.ICollectionRepository;
import org.example.deckforge.Infrastructur.ITradeLogRepository;
import org.example.deckforge.Infrastructur.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TradeService {
    private ICollectionRepository iCollectionRepository;
    private ITradeLogRepository iTradeLogRepository;
    private IUserRepository iUserRepository;
    private ICardRepository iCardRepository;

    public TradeService(ICollectionRepository iCollectionRepository,ITradeLogRepository iTradeLogRepository,IUserRepository iUserRepository,ICardRepository iCardRepository) {
        this.iCollectionRepository = iCollectionRepository;
        this.iTradeLogRepository = iTradeLogRepository;
        this.iUserRepository = iUserRepository;
        this.iCardRepository = iCardRepository;
    }

    public String generateTradeCode(User user, Card card){
        String tradeCode = UUID.randomUUID().toString().substring(0, 8);
        iCollectionRepository.updateTradeId(user, card, tradeCode);
        return tradeCode;
    }

    @Transactional
    public String redeemCard(String tradeId, User buyer){
        CardToTrade trade = iCollectionRepository.findCardToTrade(tradeId);

        if (trade == null){
            throw new IllegalArgumentException("Koden er enten ugyldig eller allerede brugt");
        }

        if (trade.getOwnerId() == buyer.getUserId()){
            throw new IllegalArgumentException("Du kan ikke indløse din egen kode");
        }
        User sellerLookUp = new User();
        sellerLookUp.setUserId(trade.getOwnerId());

        User seller = iUserRepository.findUser(sellerLookUp);

        Card card = iCardRepository.findById(trade.getCardId());

        iCollectionRepository.removeCardFromCollection(trade.getOwnerId(), trade.getCardId());
        iCollectionRepository.addCardToCollection(buyer.getUserId(), trade.getCardId());

        if (seller != null && card != null) {
            iTradeLogRepository.insertLog(buyer, seller, card);
        }

        return trade.getCardName();
    }
}
