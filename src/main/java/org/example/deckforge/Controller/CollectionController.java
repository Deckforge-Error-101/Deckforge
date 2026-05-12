package org.example.deckforge.Controller;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.CardService;
import org.example.deckforge.Service.CollectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CollectionController {

    private final CollectionService collectionService;
    private final CardService cardService;

    public CollectionController(CollectionService collectionService, CardService cardService) {
        this.collectionService = collectionService;
        this.cardService = cardService;
    }

    @PostMapping("/collection/add")
    public String addCardToCollection(@RequestParam("cardId") int cardId,
                                      @RequestParam(value = "tradeId", required = false) String tradeId,
                                      HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                collectionService.addCardToCollection(user.getUserId(), cardId, tradeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/collection";
    }

    @PostMapping("/collection/remove")
    public String removeCardFromCollection(@RequestParam("cardId") int cardId, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                collectionService.removeCardFromCollection(user.getUserId(), cardId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/collection";
    }

    @GetMapping("/collection")
    public String showCollection(HttpSession session,  Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        List<Card> userCollection = collectionService.getUserCollection(user.getUserId());
        List<Card> allCards = cardService.findAllCards();

        model.addAttribute("userCollection", userCollection);
        model.addAttribute("allCards", allCards);

        return "collection";
    }
}