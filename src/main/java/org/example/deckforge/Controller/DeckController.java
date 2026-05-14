package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.CardService;
import org.example.deckforge.Service.CollectionService;
import org.example.deckforge.Service.DeckService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DeckController {

    private final DeckService deckService;
    private final CardService cardService;
    private final CollectionService collectionService;

    public DeckController(DeckService deckService, CardService cardService, CollectionService collectionService) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.collectionService = collectionService;
    }

    @GetMapping("/decks")
    public String showMyDecks(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }

        List<Deck> myDecks = deckService.findAllDecksByUserId(user.getUserId());
        model.addAttribute("decks", myDecks);

        return "myDecks";
    }

    @GetMapping("/create")
    public String createDeckForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }

        model.addAttribute("deck", new Deck());
        return "createDeck";
    }

    @PostMapping("/create")
    public String createDeck(@ModelAttribute Deck deck, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }

        try {
            deck.setUserId(user.getUserId());
            deckService.createDeck(deck);
            return "redirect:/decks";

        } catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "createDeck";
        }
    }

    @PostMapping("/delete")
    public String deleteDeck(@ModelAttribute Deck deck, HttpSession session){
        User user = (User) session.getAttribute("user");

        if (user != null && user.isCurrentLogin()) {
            deckService.deleteDeck(deck);
        }
        return "redirect:/decks";
    }

    @GetMapping("/editDeck")
    public String showEditPage(@RequestParam int deckId, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }

        Deck deck = deckService.findDeckById(deckId);

        model.addAttribute("deck", deckService.findDeckById(deckId));
        model.addAttribute("deckCards", deckService.getCardsInDeck(deckId));
        model.addAttribute("userCollection", collectionService.findUserCollection(user.getUserId()));

        return "editDeck";
    }

    @PostMapping("/addCardToDeck")
    public String addCard(@RequestParam int deckId, @RequestParam int cardId) {
        deckService.addCardToDeck(deckId, cardId);
        return "redirect:/editDeck?deckId=" + deckId;
    }

    @PostMapping("/removeCardFromDeck")
    public String removeCard(@RequestParam int deckId, @RequestParam int cardId) {
        deckService.removeCardFromDeck(deckId, cardId);
        return "redirect:/editDeck?deckId=" + deckId;
    }

    @PostMapping("/updateVisibility")
    public String updateVisiblity(@RequestParam int deckId, @RequestParam(required = false) boolean isPublic) {
        deckService.updateDeckVisibility(deckId, isPublic);

        return "redirect:/editDeck?deckId=" + deckId;
    }

    @GetMapping("/explore")
    public String exploreDecks(Model model) {
        model.addAttribute("decks", deckService.findAllPublicDecks());
        return "explore";
    }
}
