package com.javagda24.christmaslottery.controller;

import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.Event;
import com.javagda24.christmaslottery.service.AccountService;
import com.javagda24.christmaslottery.service.EventService;
import com.javagda24.christmaslottery.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping(path = "/event/")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MembershipService membershipService;


    @GetMapping("/add")
    public String addEvent(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.findByUsername(principal.getName());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Event event = new Event();
            event.setEventCreator(account);
            model.addAttribute("event_to_edit", event);
            return "event-form";
        }
        return "redirect:/login";
    }

    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable("id") Long eventId, Model model, Principal principal, HttpServletRequest request) {
        Optional<Event> optionalEvent = eventService.findByIdAndUsername(eventId, principal.getName());
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            model.addAttribute("event_to_edit", event);
            return "event-form";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping("/add")
    public String saveEvent(@Valid Event event, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Nazwa i data nie mogą być puste!");
            model.addAttribute("event_to_edit", event);
            return "event-form";
        }
        eventService.save(event, principal.getName());
        return "redirect:/membership/list";
    }

    @GetMapping("/remove/{id}")
    public String toRemoveEvent(@PathVariable("id") Long eventId, Model model, Principal principal) {
        Optional<Event> optionalEvent = eventService.findByIdAndUsername(eventId, principal.getName());
        if (optionalEvent.isPresent()) {
            model.addAttribute("event", optionalEvent.get());
            return "event-remove";
        }
        return "redirect:/membership/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long eventId, Principal principal) {
        eventService.remove(eventId, principal.getName());
        return "redirect:/membership/list";
    }

    @GetMapping("/details/{id}")
    public String eventDetails(@PathVariable("id") Long eventId, Model model, Principal principal, HttpServletRequest request) {
        Optional<Event> optionalEvent = eventService.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            model.addAttribute("event", event);
            model.addAttribute("active_user", principal.getName());
            return "event-details";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/draw/{id}")
    public String drawTakers(@PathVariable("id") Long eventId, Principal principal) {
        membershipService.drawTakers(eventId, principal.getName());
        return "redirect:/event/details/" + eventId;
    }
}
