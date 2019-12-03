package com.javagda24.christmaslottery.service;

import com.javagda24.christmaslottery.model.Event;
import com.javagda24.christmaslottery.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {


    @Autowired
    private EventRepository eventRepository;


    public Optional<Event> findByIdAndUsername(Long eventId, String name) {
        return eventRepository.findByIdAndAndEventCreator_Username(eventId, name);
    }

    public void save(Event event, String name) {
        if (event.getEventCreator().getUsername().equals(name)) {
            eventRepository.save(event);
        }
    }


    public void remove(Long eventId, String name) {
        if (eventRepository.existsByIdAndEventCreator_Username(eventId, name)) {
            eventRepository.deleteById(eventId);
        }
    }

    public Optional<Event> findById(Long eventId) {
        return eventRepository.findById(eventId);
    }

}
