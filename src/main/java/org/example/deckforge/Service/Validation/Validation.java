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
        if (user.getUserId() < 0){
            throw new UserException("Id må ikke tilgå negative værdier");
        }
    }

    public void validateUnregisterEvent(EventRegistration registration, User user){
        if (registration.getRegistrationId() < 0){
            throw new EventRegistrationException("Fejl ved id");
        }
    }


    public void validateRegisterDeck(EventRegistration eventRegistration, Deck deck, Event event) {
        if (!deck.getFormatType().equals(event.getEventType())) {
            throw new DeckException("Dækkets format (" + deck.getFormatType() + ") matcher ikke eventets format (" + event.getEventType() + ").");
        }

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
