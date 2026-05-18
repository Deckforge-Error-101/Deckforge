package org.example.deckforge.Controller;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.TradeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CardController {
    private TradeService tradeService;

    public CardController(TradeService tradeService) {
        this.tradeService = tradeService;
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
            String cardName = tradeService.redeemCard(tradeId, buyerId);
            return "redirect:/redeem?success=" + cardName;
        } catch (IllegalArgumentException e) {
            return "redirect:/redeem?error=" + e.getMessage();
        }
    }

    @PostMapping("/generate")
    public String generateCode(@RequestParam int cardId, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        int userId = user.getUserId();

        String code = tradeService.generateTradeCode(userId, cardId);
        model.addAttribute("successMessage", "Din byttekode er : " + code);
        return "redirect:/collection?code=" + code;
    }
}
