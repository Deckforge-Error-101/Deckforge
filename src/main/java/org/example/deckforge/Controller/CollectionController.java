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

    @PostMapping("/collection/add")
    public String addCardToCollection(@ModelAttribute("card") Card card,
                                      @ModelAttribute("trade") Collection collection,
                                      HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");

            if (user != null && user.isCurrentLogin()) {

                collectionService.addCardToCollection(user, card, collection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/collection";
    }

    @PostMapping("/collection/remove")
    public String removeCardFromCollection(@ModelAttribute("card") Card card, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                collectionService.removeCardFromCollection(user, card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/collection";
    }

    @GetMapping("/collection")
    public String showCollection(HttpSession session,  Model model, @RequestParam(required = false) String code) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

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
    }

    @PostMapping("/collection/set-tradeable")
    public String setTradeable(@ModelAttribute Card card, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            collectionService.setCardAsTradeable(user, card);
        }
        return "redirect:/collection";
    }

    @PostMapping("/collection/remove-tradeable")
    public String removeTradeable(@ModelAttribute Card card, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            collectionService.removeCardFromTrade(user, card);
        }
        return "redirect:/collection";
    }

    @GetMapping("/trades/market")
    public String showTradeMarket(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/collection"; }

        List<CardToTrade> trades = collectionService.getAllAvailableTrades();
        model.addAttribute("trades", trades);
        return "tradeMarket";
    }
}