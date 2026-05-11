package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.CollectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
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
        return "redirect:/";
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
        return "redirect:/";
    }
}