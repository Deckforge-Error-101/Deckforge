package org.example.deckforge.Domain;

public class Event {
    private int eventId;
    private String title;
    private String eventType;
    private int capacity;
    private String statusType;

    public Event() {
    }

    public Event(int eventId, String titel, String eventType, int capacity, String statusType) {
        this.eventId = eventId;
        this.title = titel;
        this.eventType = eventType;
        this.capacity = capacity;
        this.statusType = "OPEN";
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        if (eventId > 0) {
            this.eventId = eventId;
        } else {
            throw new IllegalArgumentException("Id kan ikke være 0 eller negativt");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event skal have en titel");
        }
        if (title.length() > 50) {
            throw new IllegalArgumentException("Titlen må ikke være længere end 50 tegn");
        }

        this.title = title;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        if (eventType != null) {
            this.eventType = eventType;
        } else {
            throw new IllegalArgumentException("Event skal have en type");
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity > 0 && capacity <= 32) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Event må ikke være mindre end eller ligmed 0 eller større end 32");
        }
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        if ("OPEN".equals(statusType) || "FULL".equals(statusType)) {
            this.statusType = statusType;
        } else {
            throw new IllegalArgumentException("Event status skal være OPEN eller FULL");
        }
    }
}
