package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.CollectionService;
import org.example.deckforge.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final CollectionService collectionService;

    public UserController(UserService userService, CollectionService collectionService) {
        this.userService = userService;
        this.collectionService = collectionService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user != null && user.isCurrentLogin()) {
            model.addAttribute("user", user);
            return "homePage";
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginForm", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") User loginForm, Model model, HttpSession session) {
        try {
            User dbUser = userService.login(loginForm);
            session.setAttribute("user", dbUser);
            return "redirect:/";
        } catch (Exception ex) {
            model.addAttribute("loginError", "Forkert email eller password"); // Fixed variable name
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, Model model){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/createUser")
    public String showCreateUser(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, Model model) {
        try {
            int newUserId = userService.createUser(user);
            collectionService.initializeEmptyCollection(newUserId);
            return "redirect:/login";
        } catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "createUser";
        }
    }
}
