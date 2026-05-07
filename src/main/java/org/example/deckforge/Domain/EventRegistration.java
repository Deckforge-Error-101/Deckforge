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

    public void setRegistationId(int registationId) {
        if (registationId > 0) {
            this.registationId = registationId;
        }
    }

    public int getEventId() {
        return eventId;
    }

    public void int setEventId(int eventId) {
        if (eventId > 0) {
            this.eventId = eventId;
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

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        if (deckId > 0) {
            this.deckId = deckId;
        }
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        if (registrationDate != null) {
            this.registrationDate = registrationDate;
        }
    }
}
