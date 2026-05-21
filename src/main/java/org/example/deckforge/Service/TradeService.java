package org.example.deckforge.Service;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.*;
import org.example.deckforge.Service.Validation.TradeException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TradeService {
    private final ICollectionRepository iCollectionRepository;
    private final ITradeLogRepository iTradeLogRepository;
    private final IUserRepository iUserRepository;
    private final ICardRepository iCardRepository;

    public TradeService(ICollectionRepository iCollectionRepository, ITradeLogRepository iTradeLogRepository, IUserRepository iUserRepository, ICardRepository iCardRepository) {
        this.iCollectionRepository = iCollectionRepository;
        this.iTradeLogRepository = iTradeLogRepository;
        this.iUserRepository = iUserRepository;
        this.iCardRepository = iCardRepository;
    }

    public String generateTradeCode(User user, Card card) {
        try {
            String tradeCode = UUID.randomUUID().toString().substring(0, 8);
            iCollectionRepository.updateTradeId(user, card, tradeCode);
            return tradeCode;
        } catch (DataAccessException dae) {
            throw new TradeException("Der er sket en fejl ved trade koden, prøv igen senere");
        } catch (Exception Ex) {
            throw new RuntimeException("Der er sket en kritisk fejl");
        }
    }

    @Transactional
    public String redeemCard(String tradeId, User buyer) {
        try {
            CardToTrade trade = iCollectionRepository.findCardToTrade(tradeId);

            if (trade == null) {
                throw new TradeException("Koden er enten ugyldig eller allerede brugt");
            }

            if (trade.getOwnerId() == buyer.getUserId()) {
                throw new TradeException("Du kan ikke indløse din egen kode");
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
        } catch (DataAccessException dae) {
            throw new TradeException("Der er sket en fejl ved trade, prøv igen senere");
        } catch (TradeException te){
            throw new RuntimeException(te.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }
}
