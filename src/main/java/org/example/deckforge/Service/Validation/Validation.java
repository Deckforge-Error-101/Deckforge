package org.example.deckforge.Service.Validation;

import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Domain.User;
import org.springframework.stereotype.Component;

@Component
public class Validation {
    public void validateCreateUser(User user){
        if (user.getUserId() < 0){
            throw new UserException("Id må ikke tilgå negative værdier");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserException("Password skal udfyldes");
        }

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new UserException( "Password skal være mindst 8 tegn og indeholde stort bogstav, lille bogstav, tal og specialtegn");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")){
            throw new UserException("Email skal indeholde @ og .");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()){
            throw new UserException("Brugernavn skal udfyldes");
        }
    }

    public void validateLoginUser(User user){
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")){
            throw new UserException("Email skal indeholde @ og .");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserException("Password skal udfyldes");
        }

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new UserException( "Password skal være mindst 8 tegn og indeholde stort bogstav, lille bogstav, tal og specialtegn");
        }
    }

    public void validateDeleteUser(User user){
        if (user.getUserId() <= 0){
            throw new UserException("Id kan ikke tilgå negative værdier, eller være 0");
        }
    }

    public void updateUser(User user){
        if (user.getUserId() < 0){
            throw new UserException("Id må ikke tilgå negative værdier");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserException("Password skal udfyldes");
        }

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new UserException( "Password skal være mindst 8 tegn og indeholde stort bogstav, lille bogstav, tal og specialtegn");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")){
            throw new UserException("Email skal indeholde @ og .");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()){
            throw new UserException("Brugernavn skal udfyldes");
        }
    }

    public void validateCreateEvent(Event event, User user){
        if (event.getEventId() < 0){
            throw new EventException("Id kan ikke tilgå negative værdier");
        }
        if (event.getCapacity() < 0){
            throw new EventException("Kort mængde kan ikke tilgå negative værdier");
        }
        if (event.getTitle() == null || event.getTitle().isEmpty()){
            throw new EventException("Event title må ikke være tom");
        }
        if (event.getTitle().length() > 50) {
            throw new EventException("Event må ikke have et navn på mere end 50 tegn");
        }
        if (user.getUserId() < 0){
            throw new UserException("Id må ikke tilgå negative værdier");
        }

        if ("CASUAL_EVENT".equals(event.getEventType())) {
            if (event.getCapacity() > 32) {
                throw new DeckException("Der må ikke være mere end 32 pladser til et casual event");
            } else if (event.getCapacity() < 4) {
                throw new DeckException("Der skal være minimum 4 tilmeldinger");
            }
        }

        if ("COMMANDER_EVENT".equals(event.getEventType())) {
            if (event.getCapacity() >= 32) {
                throw new DeckException("Der må ikke være mere end 16 pladser til et commander event");
            } else if (event.getCapacity() < 4) {
                throw new DeckException("Der må ikke være 0 eller negative pladser til et event");
            }
        }
    }

    public void validateDeleteEvent(Event event){
        if (event.getEventId() < 0){
            throw new EventException("Der er sket en fejl ved sletning, prøv igen senere");
        }
    }


    public void validateRegisterDeck(EventRegistration registration, Deck deck, Event event) {
        if (deck.getFormatType() == null || event.getEventType() == null) {
            throw new DeckException("Format eller event-type mangler.");
        }

        //Validering af event type og deck type
        if ("COMMANDER_EVENT".equals(event.getEventType()) && !"COMMANDER".equals(deck.getFormatType())) {
            throw new DeckException("Du kan kun tilmelde et Commander-dæk til et Commander event.");
        }

        //validering af slots
        if ("COMMANDER".equals(deck.getFormatType())) {
            if (deck.getSlots() != 100) {
                throw new DeckException("Et Commander-dæk skal bestå af præcis 100 kort.");
            }
        } else if ("STANDARD".equals(deck.getFormatType())) {
            if (deck.getSlots() < 60) {
                throw new DeckException("Et Standard-dæk skal bestå af mindst 60 kort.");
            }
        }
    }
}
