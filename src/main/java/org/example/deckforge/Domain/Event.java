package org.example.deckforge.Domain;

public class Event {
    private int eventId;
    private String titel;
    private String eventType;
    private int capacity;

    public Event(){
    }

    public Event(int eventId, String titel, String eventType, int capacity) {
        this.eventId = eventId;
        this.titel = titel;
        this.eventType = eventType;
        this.capacity = capacity;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        if (eventId > 0) {
            this.eventId = eventId;
        }
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        if (titel != null) {
            this.titel = titel;
        }
    }
        //test mc testface
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        if (eventType != null) {
            this.eventType = eventType;
        }
    }

    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        }
    }
}
