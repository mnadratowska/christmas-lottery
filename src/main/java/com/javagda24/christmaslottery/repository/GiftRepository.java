package com.javagda24.christmaslottery.repository;

import com.javagda24.christmaslottery.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    Optional<Gift> findByIdAndAccount_Username(Long giftId, String username);

    List<Gift> findAllByAccount_UsernameOrderByAdditionDateDesc(String username);

    boolean existsByIdAndAccount_Username(Long giftId, String username);
}
