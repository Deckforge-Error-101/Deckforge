package org.example.deckforge.Domain;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private int deckId;
    private String deckName;
    private String formatType;
    private int slots;
    private boolean isPublic;
    private int userId;
    private List<Card> cards;

    public Deck(){
    }

    public Deck(int deckId, String deckName, String formatType, int slots, boolean isPublic, int userId) {
        this.deckId = deckId;
        this.deckName = deckName;
        this.formatType = formatType;
        this.slots = slots;
        this.isPublic = isPublic;
        this.userId = userId;
        //Listen her initialisere vi som tom så den fungere i repo'erne (vi undgår nullpointExceptions fejl)
        this.cards = new ArrayList<>();
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName){
        this.deckName = deckName;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        if (formatType != null) {
            this.formatType = formatType;
        } else {
            throw new RuntimeException("formatType må ikke være null");
            }
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

    //2 getters til public for undgå forvirreing med html
    public boolean isPublic(){
        return isPublic;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
