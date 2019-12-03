package com.javagda24.christmaslottery.controller;


import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.Comment;
import com.javagda24.christmaslottery.model.Gift;
import com.javagda24.christmaslottery.service.AccountService;
import com.javagda24.christmaslottery.service.CommentService;
import com.javagda24.christmaslottery.service.GiftService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/gift/")
public class GiftController {

    @Autowired
    private GiftService giftService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/add")
    public String addGift(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.findByUsername(principal.getName());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Gift gift = new Gift();
            gift.setAccount(account);
            model.addAttribute("gift_to_edit", gift);
            return "gift-form";
        }
        return "redirect:/login";
    }

    @GetMapping("/edit/{id}")
    public String editGift(@PathVariable("id") Long giftId, Model model, Principal principal, HttpServletRequest request) {
        Optional<Gift> optionalGift = giftService.findByIdAndUsername(giftId, principal.getName());
        if (optionalGift.isPresent()) {
            Gift gift = optionalGift.get();
            model.addAttribute("gift_to_edit", gift);
            return "gift-form";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping("/add")
    public String saveGift(@Valid Gift gift, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Nazwa nie może być pusta!");
            model.addAttribute("gift_to_edit", gift);
            return "gift-form";
        }
        giftService.save(gift, principal.getName());
        return "redirect:/gift/list";
    }

    @GetMapping("/list")
    public String giftList(Model model, Principal principal) {
        List<Gift> gifts = giftService.giftsByUsername(principal.getName());
        model.addAttribute("active_user", principal.getName());
        model.addAttribute("user", principal.getName());
        model.addAttribute("gifts", gifts);
        return "gift-list";
    }

    @GetMapping("/list/{id}")
    public String giftListOfUser(@PathVariable("id") Long userId, Model model, Principal principal, HttpServletRequest request) {
        Optional<Account> optionalAccount = accountService.getById(userId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            List<Gift> gifts = giftService.giftsByUsername(account.getUsername());
            model.addAttribute("active_user", principal.getName());
            model.addAttribute("user", account.getUsername());
            model.addAttribute("gifts", gifts);
            return "gift-list";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/remove/{id}")
    public String removeGift(@PathVariable("id") Long giftId, Model model, Principal principal) {
        Optional<Gift> optionalGift = giftService.findByIdAndUsername(giftId, principal.getName());
        if (optionalGift.isPresent()) {
            model.addAttribute("gift", optionalGift.get());
            return "gift-remove";
        }
        return "redirect:/gift/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteGift(@PathVariable("id") Long giftId, Principal principal) {
        giftService.remove(giftId, principal.getName());
        return "redirect:/gift/list";
    }

    @GetMapping("/details/{id}")
    public String giftDetais(@PathVariable("id") Long giftId, Model model, Principal principal, HttpServletRequest request) {
        Optional<Gift> optionalGift = giftService.findById(giftId);
        if (optionalGift.isPresent()) {
            Gift gift = optionalGift.get();
            List<Comment> comments = commentService.getCommentsByGift(gift);
            model.addAttribute("gift", gift);
            model.addAttribute("comments", comments);
            model.addAttribute("active_user", principal.getName());
            return "gift-details";
        }
        return "redirect:" + request.getHeader("referer");
    }
}
