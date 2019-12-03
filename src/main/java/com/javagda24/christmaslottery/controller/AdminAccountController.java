package com.javagda24.christmaslottery.controller;


import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.dto.UserPasswordResetRequest;
import com.javagda24.christmaslottery.service.AccountRoleService;
import com.javagda24.christmaslottery.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/account/")
@PreAuthorize(value = "hasRole('ADMIN')")
public class AdminAccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRoleService accountRoleService;

    @GetMapping("/list")
    public String getUserList(Model model) {

        model.addAttribute("accounts", accountService.getAll());
        return "account-list";
    }

    @GetMapping("/remove")
    public String removeAccount(@RequestParam("accountId") Long id) {
        accountService.removeById(id);
        return "redirect:/admin/account/list";
    }

    @GetMapping("/toggleLock")
    public String lockUnlock(@RequestParam("accountId") Long id) {
        accountService.toggleLock(id);
        return "redirect:/admin/account/list";
    }

    @GetMapping("/editRoles")
    public String editRoles(Model model, @RequestParam("accountId") Long id) {
        Optional<Account> accountOptional = accountService.getById(id);
        if (accountOptional.isPresent()) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("user", accountOptional.get());
            return "account-roles";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/editRoles")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.updateRoles(accountId, request);
        return "redirect:/admin/account/list";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model, @RequestParam("accountId") Long id) {
        Optional<Account> optionalAccount = accountService.getById(id);
        if (optionalAccount.isPresent()) {
            model.addAttribute("user", optionalAccount.get());
            return "account-passwordreset";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(UserPasswordResetRequest request) {
        accountService.resetPassword(request);
        return "redirect:/admin/account/list";
    }
}
