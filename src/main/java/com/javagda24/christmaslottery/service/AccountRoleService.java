package com.javagda24.christmaslottery.service;

import com.javagda24.christmaslottery.model.AccountRole;
import com.javagda24.christmaslottery.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountRoleService {

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    public List<AccountRole> getAll() {
        return accountRoleRepository.findAll();
    }
}
