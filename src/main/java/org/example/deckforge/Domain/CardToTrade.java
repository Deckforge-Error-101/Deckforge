package org.example.deckforge.Domain;

public class CardToTrade {
        private int ownerId;
        private int cardId;
        private String cardName;

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
    }
