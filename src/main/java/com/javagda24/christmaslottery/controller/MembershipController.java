package com.javagda24.christmaslottery.controller;


import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.Event;
import com.javagda24.christmaslottery.model.Membership;
import com.javagda24.christmaslottery.service.AccountService;
import com.javagda24.christmaslottery.service.EventService;
import com.javagda24.christmaslottery.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/membership/")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/list")
    public String membershipList(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.findByUsername(principal.getName());
        if (optionalAccount.isPresent()) {
            model.addAttribute("events", optionalAccount.get().getAllEvents());
            List<Membership> invitations = membershipService.getInvitations(principal.getName());
            model.addAttribute("invitations", invitations);
            model.addAttribute("active_user", principal.getName());
            return "membership-list";
        }
        return "redirect:/login";
    }

    @GetMapping("/event/{id}/join")
    public String joinYourEvent(@PathVariable("id") Long eventId, Principal principal) {
        membershipService.joinEvent(eventId, principal.getName());
        return "redirect:/membership/list";
    }

    @GetMapping("/event/{id}/invite")
    public String invitationForm(@PathVariable("id") Long eventId, Model model, Principal principal, HttpServletRequest request) {
        Optional<Event> optionalEvent = eventService.findByIdAndUsername(eventId, principal.getName());
        if (optionalEvent.isPresent()) {
            List<Account> accounts = accountService.getAll();
            model.addAttribute("active_user", principal.getName());
            model.addAttribute("accounts", accounts);
            model.addAttribute("event", optionalEvent.get());
            return "invite-form";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/event/{id}/invite/{userId}")
    public String inviteUserForEvent(@PathVariable("id") Long eventId, @PathVariable("userId") Long userId,
                                     Principal principal, HttpServletRequest request) {
        membershipService.invite(eventId, userId, principal.getName());
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/event/{id}/cancel/{userId}")
    public String cancelInvitationForEvent(@PathVariable("id") Long eventId, @PathVariable("userId") Long userId,
                                           Principal principal, HttpServletRequest request) {
        membershipService.cancel(eventId, userId, principal.getName());
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/invitation/accept/{id}")
    public String acceptInvitation(@PathVariable("id") Long membershipId, Principal principal, HttpServletRequest request) {
        membershipService.acceptInvitation(membershipId, principal.getName());
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/invitation/ignore/{id}")
    public String ignoreInvitation(@PathVariable("id") Long membershipId, Principal principal, HttpServletRequest request) {
        membershipService.ignoreInvitation(membershipId, principal.getName());
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/resign/{id}")
    public String resign(@PathVariable("id") Long eventId, Principal principal) {
        membershipService.resign(eventId, principal.getName());
        return "redirect:/membership/list";
    }

    @GetMapping("/toresign/{id}")
    public String toResign(@PathVariable("id") Long eventId, Principal principal, Model model){
        Optional<Event> optionalEvent = eventService.findById(eventId);
        if (optionalEvent.isPresent()){
            model.addAttribute("event", optionalEvent.get());
            return "membership-remove";
        }
        return "redirect:/membership/list";
    }

}
