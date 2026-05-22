package org.example.deckforge.Controller;

import org.example.deckforge.Domain.*;
import org.example.deckforge.Service.TradeService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Service.CardService;
import org.example.deckforge.Service.CollectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class CollectionController {

    private final CollectionService collectionService;
    private final CardService cardService;
    private final TradeService tradeService;

    public CollectionController(CollectionService collectionService, CardService cardService, TradeService tradeService) {
        this.collectionService = collectionService;
        this.cardService = cardService;
        this.tradeService = tradeService;
    }

    //add bruges til at tilføje et kort til collectionen i databasen
    @PostMapping("/collection/add")
    public String addCardToCollection(Model model, @ModelAttribute("card") Card card, @ModelAttribute("trade") Collection collection, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");

            if (user != null && user.isCurrentLogin()) {

                collectionService.addCardToCollection(user, card, collection);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "collection";
        }
        return "redirect:/collection";
    }

    //remove bruges til at fjerne et kort fra collection
    @PostMapping("/collection/remove")
    public String removeCardFromCollection(Model model, @ModelAttribute("card") Card card, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                collectionService.removeCardFromCollection(user, card);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "collection";
        }
        return "redirect:/collection";
    }

    //collection bruges til at vise og sorter brugerens samling af kort
    @GetMapping("/collection")
    public String showCollection(HttpSession session,  Model model, @RequestParam(required = false) String code) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }
        try {
            if (code != null && !code.isEmpty()) {
                model.addAttribute("successMessage", "Din byttekode er : " + code);
            }

            List<Card> userCollection = collectionService.getUserCollection(user);
            List<Card> allCards = cardService.findAllCards();
            List<String> rarityOrder = Arrays.asList("COMMON", "UNCOMMON", "RARE", "MYTHIC");

            //Sortering af kort i Collection.
            List<Card> sortedCollection = userCollection.stream()
                    .sorted(Comparator.comparingInt(card -> rarityOrder.indexOf(card.getCardRarity())))
                    .toList();

            //Sortering af kort i dropdown
            List<Card> sortedCards = allCards.stream()
                    .sorted(Comparator.comparingInt(card -> rarityOrder.indexOf(card.getCardRarity())))
                    .toList();

            model.addAttribute("userCollection", sortedCollection);
            model.addAttribute("allCards", sortedCards);

            return "collection";
        }catch (Exception e){
            model.addAttribute("errorMessage", "Kunne ikke hente samlingen:" + e.getMessage());
            model.addAttribute("userCollection", List.of());
            model.addAttribute("allCards", List.of());
            return "collection";
        }
    }

    //set-tradeable sætter et kort op synligt på byttemarked
    @PostMapping("/collection/set-tradeable")
    public String setTradeable(Model model, @ModelAttribute Card card, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                collectionService.setCardAsTradeable(user, card);
            }
            return "redirect:/collection";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kunne ikke hente samlingen");
            return "collection";
        }
    }

    //remove-tradeable fjerner et kort fra byttemarked
    @PostMapping("/collection/remove-tradeable")
    public String removeTradeable(Model model, @ModelAttribute Card card, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                collectionService.removeCardFromTrade(user, card);
            }
            return "redirect:/collection";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kunne ikke fjerne fra samlingen");
            return "collection";
        }
    }

    //trades/market fremvises på marked med hvilke kort er sat op til bytning
    @GetMapping("/trades/market")
    public String showTradeMarket(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/collection";
        }

        try {
            List<CardToTrade> trades = collectionService.getAllAvailableTrades();
            model.addAttribute("trades", trades);
            return "tradeMarket";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "fejl" + e.getMessage());
            return "tradeMarket";
        }
    }
}