package org.example.deckforge.Domain;

public class Deck {
    private int deckId;
    private String deckName;
    private String formatType;
    private int slots;
    private int userId;

    public Deck(){
    }
    public Deck(int deckId, String deckName, String formatType, int slots, int userId) {
        this.deckId = deckId;
        this.deckName = deckName;
        this.formatType = formatType;
        this.slots = slots;
        this.userId = userId;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        if (deckId > 0) {
            this.deckId = deckId;
        }
    }

    public String getDeckName() {
        return  deckName;
    }

    public void setDeckName(String deckName) throws Exception {
        if (deckName != null) {
            this.deckName = deckName;
        } else{ throw new Exception("Decket skal have et navn");}
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) throws Exception {
        if (formatType != null) {
            this.formatType = formatType;
        } else {throw new Exception("Decket skal have et format");}
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) throws Exception {
        if (slots > 0) {
            this.slots = slots;
        } else {throw new Exception("Decket skal have et antal slots");}
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId > 0) {
            this.userId = userId;
        }
    }
}
