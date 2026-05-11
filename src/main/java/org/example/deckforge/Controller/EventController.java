package org.example.deckforge.Controller;
import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping("/events")
    public String showEvents(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("events", eventService.findAllEvents());

        model.addAttribute("registrations",
                eventService.findRegistrationsByUserId(user.getUserId()));

        return "events";
    }

    @PostMapping("/registerEvent")
    public String registerEvent(@RequestParam int eventId,
                                HttpSession session,
                                Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {

            eventService.registerToEvent(
                    eventId,
                    user.getUserId()
            );

            return "redirect:/registrations";

        } catch (RuntimeException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("events", eventService.findAllEvents());

            return "events";
        }
    }

    @PostMapping("/unregisterEvent")
    public String unregisterEvent(@RequestParam int registrationId,
                                  HttpSession session,
                                  Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {

            eventService.unregisterFromEvent(
                    registrationId,
                    user.getUserId()
            );

            return "redirect:/registrations";

        } catch (RuntimeException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("events", eventService.findAllEvents());

            return "events";
        }
    }
    @GetMapping("/registrations")
    public String myRegistrations(HttpSession session,
                                  Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "registrations",
                eventService.findRegistrationsByUserId(user.getUserId())
        );

        return "registration";
    }
}