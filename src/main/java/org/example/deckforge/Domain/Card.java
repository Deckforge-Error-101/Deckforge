package org.example.deckforge.Domain;

public class Card {
    private int cardId;
    private String cardName;
    private String cardType;
    private String cardRarity;
    private String quantity;

    public Card(){

    }

    public Card(int cardId, String cardName, String cardType, String cardRarity) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardRarity = cardRarity;
    }

    public Card(int cardId, String cardName, String cardType, String cardRarity, String quantity) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardRarity = cardRarity;
        this.quantity = quantity;
    }

    public int getCardId() {
        return cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardRarity() {
        return cardRarity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setCardId(int cardId) {
        if (cardId > 0) {
            this.cardId = cardId;
        }
    }

    public void setCardName(String cardName) throws Exception {
        if (cardName != null) {
            this.cardName = cardName;
        } else {throw new Exception("Kortet skal have et navn");}
    }

    public void setCardType(String cardType) throws Exception {
        if (cardType != null) {
            this.cardType = cardType;
        } else {throw new Exception("Kortet skal have en type");}
    }

    public void setCardRarity(String cardRarity) throws Exception {
        if (cardRarity != null) {
            this.cardRarity = cardRarity;
        } else{ throw new Exception("Kortet skal have en rarity");}
    }
}
