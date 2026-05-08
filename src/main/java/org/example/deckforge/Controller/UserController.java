package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("member");

        if (user != null && user.isCurrentLogin()) {
            model.addAttribute("user", user);
            return "homePageLoginTrue";
        }
        return "index";
    }
     */

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
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
        } catch {

        }
    }

}
