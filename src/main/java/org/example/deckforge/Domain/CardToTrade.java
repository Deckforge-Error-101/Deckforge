package org.example.deckforge.Domain;

public class CardToTrade {
        private String tradeId;
        private String ownerName;
        private int ownerId;
        private int cardId;
        private String cardName;
        private String cardRarity;

        public CardToTrade(String tradeId, String ownerName, int ownerId, int cardId, String cardName, String cardRarity) {
            this.tradeId = tradeId;
            this.ownerName = ownerName;
            this.ownerId = ownerId;
            this.cardId = cardId;
            this.cardName = cardName;
            this.cardRarity = cardRarity;
        }

        public CardToTrade(int ownerId, int cardId, String cardName) {
            this.ownerId = ownerId;
            this.cardId = cardId;
            this.cardName = cardName;
        }

        public int getOwnerId() {
            return ownerId;
        }

        public int getCardId() {
            return cardId;
        }

        public String getCardName() {
            return cardName;
        }

        public String getTradeId() {
            return tradeId;
        }

        public String getCardRarity() {
            return cardRarity;
        }

        public String getOwnerName() {
            return ownerName;
        }
    }
