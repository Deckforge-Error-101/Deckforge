package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.*;
import org.example.deckforge.Service.EventRegistrationService;
import org.example.deckforge.Service.EventService;
import org.example.deckforge.Service.Validation.DeckException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    private final EventService eventService;
    private final EventRegistrationService eventRegistrationService;

    public EventController(EventService eventService,
                           EventRegistrationService eventRegistrationService) {
        this.eventService = eventService;
        this.eventRegistrationService = eventRegistrationService;
    }

    @GetMapping("/createEvent")
    public String showCreateEventPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(user.getRoleType()) && !"MANAGER".equals(user.getRoleType())) {
            return "redirect:/events";
        }
        model.addAttribute("event", new Event());
        return "createEvent";
    }

    @PostMapping("/createEvent")
    public String createEvent(@ModelAttribute Event event,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(user.getRoleType()) && !"MANAGER".equals(user.getRoleType())) {
            return "redirect:/events";
        }

        try {
            eventService.createEvent(user, event);
            return "redirect:/events";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("event", event);
            return "createEvent";
        }
    }

    @GetMapping("/events")
    public String showEvents(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("events", eventService.findAllEvents());
        return "events";
    }

    @PostMapping("/registerEvent")
    public String registerEvent(@ModelAttribute Event event,
                                HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            eventRegistrationService.registerToEvent(event, user);
            return "redirect:/registrations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("events", eventService.findAllEvents());
            return "events";
        }
    }

    @GetMapping("/registrations")
    public String myRegistrations(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("registrations", eventRegistrationService.findRegistrationsByUser(user));

        return "registration";
    }

    @PostMapping("/addDeckToRegistration")
    public String addDeckToRegistration(@ModelAttribute EventRegistration registration, @ModelAttribute("deck") Deck deck,
                                        HttpSession session,
                                        Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            registration.setUserId(user.getUserId());

            eventRegistrationService.addDeckToRegistration(registration, deck);

            return "redirect:/registrations";
        } catch (DeckException de){
            model.addAttribute("errorMessage", de.getMessage());
            model.addAttribute("registrations", eventRegistrationService.findRegistrationsByUser(user));
            return "registration";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("registrations", eventRegistrationService.findRegistrationsByUser(user));

            return "registration";
        }
    }

    @PostMapping("/unregisterEvent")
    public String unregisterEvent(@ModelAttribute EventRegistration registration,
                                  HttpSession session,
                                  Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            registration.setUserId(user.getUserId());

            eventRegistrationService.unregisterFromEvent(registration);

            return "redirect:/registrations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("registrations", eventRegistrationService.findRegistrationsByUser(user));
            return "registration";
        }
    }

    @GetMapping("/updateEvent")
    public String updateEventPage(@ModelAttribute Event event, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(user.getRoleType()) && !"MANAGER".equals(user.getRoleType())) {
            return "redirect:/events";
        }
        try {
            Event fullEvent = eventService.findEvent(event);

            if (fullEvent == null) {
                return "redirect:/events";
            }

            model.addAttribute("event", fullEvent);
            return "updateEvent";
        } catch (Exception x){
            model.addAttribute("errorMessage", x.getMessage());
            return "updateEvent";
        }
    }

    @PostMapping("/updateEvent")
    public String updateEvent(@ModelAttribute Event event,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRoleType()) && !"MANAGER".equals(user.getRoleType())) {
            return "redirect:/events";
        }

        try {
            eventService.updateEvent(user, event);
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved opdatering af event: " + e.getMessage());
            model.addAttribute("events", eventService.findAllEvents());
            return "events";
        }
    }

    @PostMapping("/deleteEvent")
    public String deleteEvent(@ModelAttribute Event event,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRoleType()) && !"MANAGER".equals(user.getRoleType())) {
            return "redirect:/events";
        }

        try {
            eventService.deleteEvent(event);
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved sletning af event: " + e.getMessage());
            model.addAttribute("events", eventService.findAllEvents());
            return "events";
        }
    }
}