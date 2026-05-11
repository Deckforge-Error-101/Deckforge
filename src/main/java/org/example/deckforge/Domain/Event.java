package org.example.deckforge.Domain;

public class Event {
    private int eventId;
    private String title;
    private String eventType;
    private int capacity;

    public Event(){
    }

    public Event(int eventId, String titel, String eventType, int capacity) {
        this.eventId = eventId;
        this.title = titel;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws Exception {
        if (title != null) {
            this.title = title;

        } else {
            throw new Exception("Event skal have en titel");}
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) throws Exception {
        if (eventType != null) {
            this.eventType = eventType;
        } else{ throw new Exception("Event skal have en type");}
    }

    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) throws Exception {
        if (capacity > 0) {
            this.capacity = capacity;
        } else{ throw new Exception("Event skal have en max capacitet");}
    }
}
