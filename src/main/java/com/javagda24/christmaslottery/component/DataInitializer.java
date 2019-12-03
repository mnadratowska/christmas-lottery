package com.javagda24.christmaslottery.component;

import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.AccountRole;
import com.javagda24.christmaslottery.repository.AccountRepository;
import com.javagda24.christmaslottery.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${default.roles}")
    private String[] roles;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (String role : roles) {
            addRole(role);
        }

        addUser("admin", "admin", "ADMIN", "USER");
        addUser("user", "user", "USER");

    }

    private void addUser(String username, String password, String... roles) {
        if (!accountRepository.existsByUsername(username)){
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(password));
            Set<AccountRole> userRoles = findRoles(roles);
            account.setRoles(userRoles);
            accountRepository.save(account);
        }
    }

    private Set<AccountRole> findRoles(String[] roles) {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : roles){
            accountRoleRepository.findByName(role).ifPresent(accountRoles::add);
        }
        return accountRoles;
    }

    private void addRole(String roleToCreate) {
        if (!accountRoleRepository.existsByName(roleToCreate)){
            AccountRole accountRole = new AccountRole();
            accountRole.setName(roleToCreate);
            accountRoleRepository.save(accountRole);
        }
    }
}
