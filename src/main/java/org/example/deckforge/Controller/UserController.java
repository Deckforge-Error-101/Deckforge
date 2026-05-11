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


    public UserController(UserService userService) {
        this.userService = userService;
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
            ex.printStackTrace();
            model.addAttribute("Login error", "Forkert email eller password");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, Model model){
        try {
            session.invalidate();
            return "redirect:/";
        } catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, Model model) {
        try {
            userService.createUser(user);
            return "redirect:/";
        } catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
        }
        return "createUser";
    }

    @PostMapping("homePage")
    public String homePage(Model model){
        return "homePage";
    }


}
