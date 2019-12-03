package com.javagda24.christmaslottery.repository;

import com.javagda24.christmaslottery.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByGiver_UsernameAndGiverAcceptance(String username, boolean acceptance);

    boolean existsByEvent_IdAndGiver_Id(Long eventId, Long userId);

    Membership getByEvent_IdAndGiver_Id(Long eventId, Long userId);

    boolean existsByEvent_IdAndGiver_Username(Long eventId, String username);

    Membership getByEvent_IdAndGiver_Username(Long eventId, String username);

}
