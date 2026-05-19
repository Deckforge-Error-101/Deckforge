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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /* Vi bruger ikke denne metode pt.
    @GetMapping("/decks")
    public String showMyDecks(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        List<Deck> myDecks = deckService.findAllDecksByUserId(user.getUserId());
        model.addAttribute("decks", myDecks);

        return "myDecks";
    }
     */

    @GetMapping("/createDeck")
    public String createDeckForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        model.addAttribute("deck", new Deck());
        return "createDeck";
    }

    @PostMapping("/createDeck")
    public String createDeck(@ModelAttribute Deck deck, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        try {
            deck.setUserId(user.getUserId());
            deckService.createDeck(deck);

            return "redirect:/";

        } catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "createDeck";
        }
    }

    @GetMapping("/delete")
    public String deleteDeck() {
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteDeck(@ModelAttribute Deck deck, HttpSession session){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        deckService.deleteDeck(deck);

        return "redirect:/";
    }

    @PostMapping("/editDeck")
    public String showEditPage(@ModelAttribute Deck deck, HttpSession session, Model model){
        System.out.println("Modtaget dæk-ID i controller: " + deck.getDeckId());
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }

        Deck fullDeck = deckService.findDeckById(deck);

        model.addAttribute("deck", fullDeck);
        model.addAttribute("deckCards", deckService.getCardsInDeck(fullDeck));
        model.addAttribute("userCollection", collectionService.findUserCollection(user));

        return "editDeck";
    }

    @GetMapping("/addCardToDeck")
    public String addCard() {
        return "redirect:/";
    }

    @PostMapping("/addCardToDeck")
    public String addCard(Model model, HttpSession session, @ModelAttribute Deck deck, @ModelAttribute Card card) {

        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }
        try {
            deckService.addCardToDeck(user, deck, card);
        } catch (IllegalStateException ex){
            model.addAttribute("error", ex.getMessage());
        }
        return "forward:/editDeck";
    }

    @GetMapping("/removeCardFromDeck")
    public String removeCardFromDeck() {
        return "redirect:/";
    }

    @PostMapping("/removeCardFromDeck")
    public String removeCard(HttpSession session, @ModelAttribute Deck deck, @ModelAttribute Card card) {
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        deckService.removeCardFromDeck(deck, card);
        return "forward:/editDeck";
    }

    @GetMapping("/updateVisibility")
    public String updateVisibility() {
        return "redirect:/";
    }

    @PostMapping("/updateVisibility")
    public String updateVisiblity(HttpSession session, @ModelAttribute Deck deck) {
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }
        deckService.updateDeckVisibility(deck);

        return "forward:/editDeck" + deck.getDeckId();
    }

    @GetMapping("/explore")
    public String exploreDecks(Model model) {
        model.addAttribute("decks", deckService.findAllPublicDecks());
        return "explore";
    }

    @GetMapping("/updateDeck")
    public String updateDeck() {
        return "redirect:/";
    }

    @PostMapping("/updateDeck")
    public String updateDeck(HttpSession session, @ModelAttribute Deck deck) {
        System.out.println("UPDATE DECK KALDET!");
        System.out.println("Dæk ID: " + deck.getDeckId());
        System.out.println("Er dækket markeret som public? " + deck.isPublic());

        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        deckService.updateDeck(deck);

        return "forward:/editDeck";
    }
}
