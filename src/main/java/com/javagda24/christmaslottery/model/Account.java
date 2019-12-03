package com.javagda24.christmaslottery.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private boolean locked;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = {org.hibernate.annotations.CascadeType.DETACH})
    private Set<AccountRole> roles;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "giver", orphanRemoval = true)
    private Set<Membership> memberships;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "taker", orphanRemoval = true)
    private Set<Membership> membershipsAsTaker;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private Set<Gift> gifts;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "eventCreator", orphanRemoval = true)
    private Set<Event> events;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private Set<Comment> comments;

    public boolean isAdmin() {
        return roles.stream().map(AccountRole::getName).anyMatch(s -> s.equals("ADMIN"));
    }

    public List<Event> getAllEvents() {
        Set<Event> allEvents = memberships.stream()
                .filter(membership -> membership.isGiverAcceptance())
                .map(membership -> membership.getEvent())
                .collect(Collectors.toSet());
        allEvents.addAll(events);
        return allEvents.stream().sorted((o1, o2) -> timeComparator(o1, o2)).collect(Collectors.toList());
    }

    private int timeComparator(Event e1, Event e2) {
        if (e1.getDate().after(e2.getDate())) return 1;
        else return -1;
    }

}
