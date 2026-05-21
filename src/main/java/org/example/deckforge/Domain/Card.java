package org.example.deckforge.Domain;

public class Card {
    private int cardId;
    private String cardName;
    private String cardType;
    private String cardRarity;
    private int quantity;
    private String tradeId;
    private String setType;

    public Card(){
    }

    public Card(int cardId, String cardName, String cardType, String cardRarity, String setType) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardRarity = cardRarity;
        this.setType = setType;
    }

    public Card(int cardId, String cardName, String cardType, String cardRarity, int quantity, String setType) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardRarity = cardRarity;
        this.quantity = quantity;
        this.setType = setType;
    }

    public Card(int cardId, String cardName, String cardType, String cardRarity, int quantity, String tradeId, String setType) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardRarity = cardRarity;
        this.quantity = quantity;
        this.tradeId = tradeId;
        this.setType = setType;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        if (cardId > 0) {
            this.cardId = cardId;
        } else {
            throw new IllegalArgumentException("Id kan ikke være 0 eller negativt");
        }
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        if (cardName != null && !cardName.isBlank()) {
            this.cardName = cardName;
        } else {
            throw new IllegalArgumentException("Kortet skal have et navn");
        }
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        if (cardType != null && !cardType.isBlank()) {
            this.cardType = cardType;
        } else {
            throw new IllegalArgumentException("Kortet skal have en type");
        }
    }

    public String getCardRarity() {
        return cardRarity;
    }

    public void setCardRarity(String cardRarity) {
        if (cardRarity != null && !cardRarity.isBlank()) {
            this.cardRarity = cardRarity;
        } else {
            throw new IllegalArgumentException("Kortet skal have en rarity");
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Antal kan ikke være negativt");
        }
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        if (setType != null && !setType.isBlank()) {
            this.setType = setType;
        } else {
            throw new IllegalArgumentException("Kortet skal have et set");
        }
    }
}
