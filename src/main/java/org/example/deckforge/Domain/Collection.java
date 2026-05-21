package org.example.deckforge.Domain;

public class Collection {
    private int collectionId;
    private int userId;
    private String tradeId;
    private int cardId;

    public Collection(){
    }

    public Collection(int collectionId, int userId, String tradeId, int cardId) {
        this.collectionId = collectionId;
        this.userId = userId;
        this.tradeId = tradeId;
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId > 0) {
            this.userId = userId;
        } else {
            throw new IllegalArgumentException("Fejl ved user");
        }
    }

    public String getTradeId() {
        return tradeId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        if (cardId > 0) {
            this.cardId = cardId;
        } else {
            throw new IllegalArgumentException("Fejl ved card");
        }
    }

}
