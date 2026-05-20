package org.example.deckforge.Controller;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Event;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.TradeService;
import org.example.deckforge.Service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CardController {
    private TradeService tradeService;
    private final CardService cardService;

    public CardController(TradeService tradeService, CardService cardService) {
        this.tradeService = tradeService;
        this.cardService = cardService;
    }

    @GetMapping("/redeem")
    public String showRedeemPage(HttpSession session, Model model, @RequestParam(required = false) String success, @RequestParam(required = false) String error) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        if (success != null) {
            model.addAttribute("successMessage", "Succes! Du har modtaget: " + success);
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "trade-redeem";
    }

    @PostMapping("/redeem")
    public String processRedeem(@RequestParam String tradeId, HttpSession session, Model model){
        User buyer = (User) session.getAttribute("user");

        if (buyer == null) {
            return "redirect:/";
        }

        int buyerId = buyer.getUserId();

        try {
            String cardName = tradeService.redeemCard(tradeId, buyer);
            return "redirect:/redeem?success=" + cardName;
        } catch (IllegalArgumentException e) {
            return "redirect:/redeem?error=" + e.getMessage();
        }
    }

    @PostMapping("/generate")
    public String generateCode(@ModelAttribute Card card, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        int userId = user.getUserId();

        String code = tradeService.generateTradeCode(user, card);
        model.addAttribute("successMessage", "Din byttekode er : " + code);
        return "redirect:/collection?code=" + code;
    }
    @PostMapping("/createCard")
    public String createCard(@ModelAttribute Card card,
                              HttpSession session,
                              Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRoleType())) {
            return "redirect:/";
        }
        try {
            cardService.createCard(card);
            return "redirect:/";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("card", card);
            return "createCard";
        }
    }
    @GetMapping("/createCard")
    public String showCreateCardPage(HttpSession session,
                                     Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRoleType())) {
            return "redirect:/";
        }

        model.addAttribute("card", new Card());

        return "createCard";
    }




}
