package com.javagda24.christmaslottery.controller;


import com.javagda24.christmaslottery.model.Membership;
import com.javagda24.christmaslottery.model.dto.UserRegistrationRequest;
import com.javagda24.christmaslottery.service.AccountService;
import com.javagda24.christmaslottery.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String getIndexPage(Model model, Principal principal) {
        if (principal != null) {
            List<Membership> memberships = membershipService.getMemberships(principal.getName());
            int numberOfInvitations = membershipService.numberOfInvitations(principal.getName());
            model.addAttribute("memberships", memberships);
            model.addAttribute("number_of_invitations", numberOfInvitations);
            model.addAttribute("active_user", principal.getName());
            return "main-page";
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register-form";
    }

    @PostMapping("/register")
    public String register(Model model, @Valid UserRegistrationRequest request,
                           BindingResult bindingResult) {
        if (!request.arePasswordsEqual()) {
            model.addAttribute("errorMessage", "Hasła nie są identyczne.");
            return "register-form";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Login/hasło jest za krótkie.");
            return "register-form";
        }
        if (!accountService.register(request)) {
            model.addAttribute("errorMessage", "Ten login jest już zajęty.");
            return "register-form";
        }
        return "redirect:/login";
    }


}
