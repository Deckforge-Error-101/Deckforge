package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.CardService;
import org.example.deckforge.Service.CollectionService;
import org.example.deckforge.Service.DeckService;
import org.example.deckforge.Service.Validation.DeckException;
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


    @GetMapping("/createDeck")
    public String createDeckForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        model.addAttribute("deck", new Deck());
        return "createDeck";
    }

    @PostMapping("/createDeck")
    public String createDeck(@ModelAttribute Deck deck, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        try {
            deck.setUserId(user.getUserId());
            deckService.createDeck(deck);

            return "redirect:/";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "createDeck";
        }
    }

    @GetMapping("/delete")
    public String deleteDeck() {
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteDeck(@ModelAttribute Deck deck, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }
        try {
            deckService.deleteDeck(deck);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "homePage";
        }
    }
    @GetMapping("/editDeck")
    public String editDeckForm(HttpSession session, Model model) {
        return "/editDeck";
    }


    @PostMapping("/editDeck")
    public String showEditPage(@ModelAttribute Deck deck, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/login";
        }
        try {
            Deck fullDeck = deckService.findDeckById(deck);
            if (deck.getDeckId() == 0) {
                model.addAttribute("errorMessage", "Fejl: Systemet modtog intet dæk-ID.");
                return "homePage";
            }

            model.addAttribute("deck", fullDeck);
            model.addAttribute("deckCards", deckService.getCardsInDeck(fullDeck));
            model.addAttribute("userCollection", collectionService.findUserCollection(user));

            return "editDeck";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kunne ikke åbne redigeringssiden: ");
            return "homePage";
        }
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
            return "forward:/editDeck";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());

            try {
                Deck fullDeck = deckService.findDeckById(deck);
                model.addAttribute("deck", fullDeck);
                model.addAttribute("deckCards", deckService.getCardsInDeck(fullDeck));
                model.addAttribute("userCollection", collectionService.findUserCollection(user));
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Fejl ved tilføjelse af kort til deck");
                return "editDeck";
            }
            return "editDeck";
        }
    }

    @GetMapping("/removeCardFromDeck")
    public String removeCardFromDeck() {
        return "redirect:/";
    }

    @PostMapping("/removeCardFromDeck")
    public String removeCard(Model model, HttpSession session, @ModelAttribute Deck deck, @ModelAttribute Card card) {
        try {
            User user = (User) session.getAttribute("user");

            if (user == null || !user.isCurrentLogin()) {
                return "redirect:/";
            }

            deckService.removeCardFromDeck(deck, card);
            return "forward:/editDeck";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved fjernelse af kort");
            return "editDeck";
        }
    }

    @GetMapping("/updateVisibility")
    public String updateVisibility() {
        return "redirect:/";
    }

    @PostMapping("/updateVisibility")
    public String updateVisiblity(Model model, HttpSession session, @ModelAttribute Deck deck) {
        try {
            User user = (User) session.getAttribute("user");

            if (user == null || !user.isCurrentLogin()) {
                return "redirect:/login";
            }
            deckService.updateDeckVisibility(deck);

            return "forward:/editDeck" + deck.getDeckId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved opdatering af synlighed");
            return "editDeck";
        }
    }

    @GetMapping("/explore")
    public String exploreDecks(Model model) {
        try {
            model.addAttribute("decks", deckService.findAllPublicDecks());
            return "explore";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved community decks");
            return "explore";
        }
    }

    @GetMapping("/updateDeck")
    public String updateDeck() {
        return "redirect:/";
    }

    @PostMapping("/updateDeck")
    public String updateDeck(HttpSession session, @ModelAttribute Deck deck, Model model) {
        try {
            User user = (User) session.getAttribute("user");

            if (user == null || !user.isCurrentLogin()) {
                return "redirect:/";
            }

            deckService.updateDeck(deck);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved opdatering af deck");
            return "editDeck";
        }
        return "forward:/editDeck";
    }
}
