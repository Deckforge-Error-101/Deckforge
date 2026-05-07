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

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        if (collectionId > 0) {
            this.collectionId = collectionId;
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId > 0) {
            this.userId = userId;
        }
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) throws Exception {
        if (tradeId != null) {
            this.tradeId = tradeId;
        } throw new Exception("Der skal være en trade id");
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        if (cardId > 0) {
            this.cardId = cardId;
        }
    }

}
