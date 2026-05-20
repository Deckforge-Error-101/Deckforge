package org.example.deckforge.Domain;

import java.time.LocalDateTime;

public class TradeLog {
    private int logId;
    private int buyerId;
    private int sellerId;
    private int cardId;
    private LocalDateTime tradedAt;

    public TradeLog(int buyerId, int sellerId, int cardId) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.cardId = cardId;
    }

    public int getLogId() { return logId; }
    public int getBuyerId() { return buyerId; }
    public int getSellerId() { return sellerId; }
    public int getCardId() { return cardId; }
    public LocalDateTime getTradedAt() { return     tradedAt; }

}
