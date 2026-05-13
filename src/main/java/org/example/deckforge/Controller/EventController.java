package org.example.deckforge.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.deckforge.Domain.Event;
import org.example.deckforge.Domain.EventRegistration;
import org.example.deckforge.Domain.User;
import org.example.deckforge.Service.EventRegistrationService;
import org.example.deckforge.Domain.RoleType;
import org.example.deckforge.Service.EventService;
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
    public String showCreateEventPage(HttpSession session,
                                      Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRoleType())) {
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

        if (!"ADMIN".equals(user.getRoleType())) {
            return "redirect:/events";
        }
        try {
            eventService.createEvent(event);
            return "redirect:/events";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("event", event);
            return "createEvent";
        }
    }

    @GetMapping("/events")
    public String showEvents(HttpSession session,
                             Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("events", eventService.findAllEvents());

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

        EventRegistration registration = new EventRegistration();

        try {

            registration.setEventId(eventId);
            registration.setUserId(user.getUserId());
            eventRegistrationService.registerToEvent(registration);

            return "redirect:/registrations";

        } catch (Exception e) {

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
        EventRegistration registration = new EventRegistration();

        try {

            registration.setRegistrationId(registrationId);
            registration.setUserId(user.getUserId());eventRegistrationService.unregisterFromEvent(registration);
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
        model.addAttribute("registrations", eventRegistrationService.findRegistrationsByUserId(user.getUserId()));
        return "registration";
    }
}