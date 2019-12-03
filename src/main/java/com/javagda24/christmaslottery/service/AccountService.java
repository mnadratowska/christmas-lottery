package com.javagda24.christmaslottery.service;

import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.AccountRole;
import com.javagda24.christmaslottery.model.dto.UserPasswordResetRequest;
import com.javagda24.christmaslottery.model.dto.UserRegistrationRequest;
import com.javagda24.christmaslottery.repository.AccountRepository;
import com.javagda24.christmaslottery.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Value("${default.register.roles}")
    private String[] registerRoles;


    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public void removeById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (!account.isAdmin()) {
                accountRepository.deleteById(id);
            }
        }
    }


    public void save(Account account) {
        accountRepository.save(account);
    }

    public void toggleLock(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            boolean locked = account.isLocked();
            account.setLocked(!locked);
            accountRepository.save(account);
        }
    }

    public boolean register(UserRegistrationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            return false;
        }
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        account.setRoles(findRolesByName(registerRoles));
        accountRepository.save(account);
        return true;
    }

    public Set<AccountRole> findRolesByName(String... roles) {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : roles) {
            accountRoleRepository.findByName(role).ifPresent(accountRoles::add);

        }
        return accountRoles;
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public void updateRoles(Long accountId, HttpServletRequest request) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<AccountRole> accountRoles = new HashSet<>();
            for (String key : parameterMap.keySet()) {
                accountRoleRepository.findByName(key).ifPresent(accountRoles::add);
            }
            account.setRoles(accountRoles);
            accountRepository.save(account);
        }
    }

    public void resetPassword(UserPasswordResetRequest request) {
        Optional<Account> accountOptional = accountRepository.findById(request.getAccountId());
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setPassword(passwordEncoder.encode(request.getPassword()));

            accountRepository.save(account);
        }
    }

    public Optional<Account> findByUsername(String name) {
        return accountRepository.findByUsername(name);
    }
}
