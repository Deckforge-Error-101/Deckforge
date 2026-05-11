package org.example.deckforge.Domain;

import java.time.LocalDateTime;

public class EventRegistration {
    private int registationId;
    private int eventId;
    private int userId;
    private int deckId;
    private LocalDateTime registrationDate;

    public EventRegistration() {
    }
    public EventRegistration(int registationId, int eventId, int userId, int deckId, LocalDateTime registrationDate) {
        this.registationId = registationId;
        this.eventId = eventId;
        this.userId = userId;
        this.deckId = deckId;
        this.registrationDate = registrationDate;
    }

    public int getRegistationId() {
        return registationId;
    }

    public void setRegistationId(int registationId) throws Exception {
        if (registationId > 0) {
            this.registationId = registationId;
        } else {
            throw new Exception ("Fejl ved event");
        }
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) throws Exception {
        if (eventId > 0) {
            this.eventId = eventId;
        } else {
            throw new Exception ("fejl ved Event");
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) throws Exception {
        if (userId > 0) {
            this.userId = userId;
        } else {
            throw new Exception("Fejl ved event");
        }
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) throws Exception {
        if (deckId > 0) {
            this.deckId = deckId;
        } else {
            throw new Exception("Fejl ved event");
        }
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) throws Exception {
        if (registrationDate != null) {
            this.registrationDate = registrationDate;
        } else{
            throw new Exception("Der skal sættes en dato");
        }
    }
}
