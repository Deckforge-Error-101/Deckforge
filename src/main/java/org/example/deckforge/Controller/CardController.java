package org.example.deckforge.Controller;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Service.Validation.TradeException;
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

        try {
            User user = (User) session.getAttribute("user");

            if (user == null || !user.isCurrentLogin()) {
                return "redirect:/";
            }

            if (success != null && !success.isBlank()) {
                model.addAttribute("successMessage", "Succes! Du har modtaget: " + success);
            }

            if (error != null && !error.isBlank()) {
                model.addAttribute("errorMessage", error);
            }

            return "trade-redeem";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kunne ikke åbne siden.");
            return "index";
        }
    }

    @PostMapping("/redeem")
    public String processRedeem(@RequestParam String tradeId, HttpSession session, Model model){
        User buyer = (User) session.getAttribute("user");

        if (buyer == null || !buyer.isCurrentLogin()) {
            return "redirect:/";
        }

        try {
            String cardName = tradeService.redeemCard(tradeId, buyer);
            return "redirect:/redeem?success=" + cardName;
        } catch (TradeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "trade-redeem";
        } catch (Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
            return "trade-redeem";
        }
    }

    @GetMapping("/generate")
    public String generateCode(User user){
        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }
        return "redirect:/redeem";
    }

    @PostMapping("/generate")
    public String generateCode(@ModelAttribute Card card, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }
        try{
        String code = tradeService.generateTradeCode(user, card);
        model.addAttribute("successMessage", "Din byttekode er : " + code);
        return "redirect:/collection?code=" + code;}
        catch (Exception e){
           return "redirect:/collection";
        }
    }
    @PostMapping("/createCard")
    public String createCard(@ModelAttribute Card card, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        if (!"ADMIN".equals(user.getRoleType())) {
            return "redirect:/";
        }
        try {
            System.out.println("Card name: " + card.getCardName());
            System.out.println("Card type: " + card.getCardType());
            System.out.println("Rarity: " + card.getCardRarity());
            System.out.println("Set type: " + card.getSetType());
            cardService.createCard(card);
            return "redirect:/";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("card", card);
            return "createCard";
        }
    }
    @GetMapping("/createCard")
    public String showCreateCardPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null || !user.isCurrentLogin()) {
            return "redirect:/";
        }

        if (!"ADMIN".equals(user.getRoleType())) {
            return "redirect:/";
        }

        try {
            model.addAttribute("card", new Card());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "createCard";
        }
        return "createCard";
    }




}
