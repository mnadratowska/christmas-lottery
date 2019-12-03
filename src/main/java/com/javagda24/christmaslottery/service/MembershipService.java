package com.javagda24.christmaslottery.service;

import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.Event;
import com.javagda24.christmaslottery.model.Membership;
import com.javagda24.christmaslottery.repository.AccountRepository;
import com.javagda24.christmaslottery.repository.EventRepository;
import com.javagda24.christmaslottery.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EventRepository eventRepository;

    private int sortByEventDate(Membership m1, Membership m2) {
        LocalDate date1 = m1.getEvent().getDate().toLocalDate();
        LocalDate date2 = m2.getEvent().getDate().toLocalDate();
        if (date1.isBefore(date2)) {
            return -1;
        }
        return 1;
    }

    public List<Membership> getInvitations(String name) {
        return membershipRepository.findAllByGiver_UsernameAndGiverAcceptance(name, false);
    }

    public int numberOfInvitations(String name) {
        return getInvitations(name).size();
    }

    public List<Membership> getMemberships(String name) {
        List<Membership> membershipList = membershipRepository.findAllByGiver_UsernameAndGiverAcceptance(name, true);
        membershipList.sort((o1, o2) -> sortByEventDate(o1, o2));

        return membershipList;
    }

    public void invite(Long eventId, Long userId, String name) {
        if (membershipRepository.existsByEvent_IdAndGiver_Id(eventId, userId)) {
            return;
        }
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Optional<Account> optionalAccount = accountRepository.findById(userId);

        if (optionalEvent.isPresent() && optionalAccount.isPresent()) {
            Event event = optionalEvent.get();
            if (event.getEventCreator().getUsername().equals(name)) {
                Account account = optionalAccount.get();
                Membership membership = new Membership();
                membership.setEvent(event);
                membership.setGiver(account);
                membershipRepository.save(membership);
            }
        }
    }

    public void cancel(Long eventId, Long userId, String name) {
        if (membershipRepository.existsByEvent_IdAndGiver_Id(eventId, userId)) {
            Membership membership = membershipRepository.getByEvent_IdAndGiver_Id(eventId, userId);
            Event event = membership.getEvent();
            if (event.getEventCreator().getUsername().equals(name) && !event.isDrawn()) {
                membershipRepository.delete(membership);
            }
        }
    }

    public void acceptInvitation(Long membershipId, String name) {
        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        if (optionalMembership.isPresent()) {
            Membership membership = optionalMembership.get();
            if (membership.getGiver().getUsername().equals(name)) {
                membership.setGiverAcceptance(true);
                membershipRepository.save(membership);
            }
        }
    }

    public void ignoreInvitation(Long membershipId, String name) {
        if (membershipRepository.existsById(membershipId)) {
            Membership membership = membershipRepository.getOne(membershipId);
            Event event = membership.getEvent();
            if (membership.getGiver().getUsername().equals(name) && !event.isDrawn()) {
                membershipRepository.deleteById(membershipId);
            }
        }
    }

    public void drawTakers(Long eventId, String name) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            if (!event.isDrawn() && event.numberOfInvitations() == 0) {
                List<Membership> acceptedMemberships = event.getAcceptedMemberships();
                Collections.shuffle(acceptedMemberships);
                int size = acceptedMemberships.size();
                for (int i = 0; i < size; i++) {
                    Membership membership = acceptedMemberships.get(i);
                    membership.setTaker(acceptedMemberships.get((i + 1) % size).getGiver());
                    membershipRepository.save(membership);
                    event.setDrawn(true);
                    eventRepository.save(event);
                }
            }
        }
    }


    public void joinEvent(Long eventId, String name) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            boolean isCreator = event.getEventCreator().getUsername().equals(name);
            Long creatorId = event.getEventCreator().getId();
            if (isCreator && !event.isDrawn() && !membershipRepository.existsByEvent_IdAndGiver_Id(eventId, creatorId)) {
                Membership membership = new Membership();
                membership.setEvent(event);
                membership.setGiver(event.getEventCreator());
                membership.setGiverAcceptance(true);
                membershipRepository.save(membership);
            }
        }
    }

    public void resign(Long eventId, String name) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            if (membershipRepository.existsByEvent_IdAndGiver_Username(eventId, name) && !event.isDrawn()) {
                Membership membership = membershipRepository.getByEvent_IdAndGiver_Username(eventId, name);
                membershipRepository.delete(membership);
            }
        }
    }
}
