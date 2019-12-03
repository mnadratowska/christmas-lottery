package com.javagda24.christmaslottery.repository;

import com.javagda24.christmaslottery.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndAndEventCreator_Username(Long eventId, String username);

    boolean existsByIdAndEventCreator_Username(Long id, String username);
}
