package com.javagda24.christmaslottery.repository;

import com.javagda24.christmaslottery.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    boolean existsByName(String name);

    Optional<AccountRole> findByName(String role);
}
