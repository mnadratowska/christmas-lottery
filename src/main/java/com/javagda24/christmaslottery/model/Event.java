package com.javagda24.christmaslottery.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String description;

    private Date date;

    private int amount;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private Set<Membership> memberships;

    @ManyToOne
    private Account eventCreator;

    private boolean drawn;

    public List<Membership> getAcceptedMemberships() {
        return memberships.stream()
                .filter(membership -> membership.isGiverAcceptance())
                .collect(Collectors.toList());
    }

    public long numberOfInvitations() {
        return memberships.stream()
                .filter(membership -> !membership.isGiverAcceptance()).count();
    }

    public Account getTakerByGiver(String giverUsername) {
        for (Membership membership : memberships) {
            if (membership.getGiver().getUsername().equals(giverUsername)) {
                return membership.getTaker();
            }
        }
        return null;
    }


    public boolean hasInvitation(String giverUsername) {
        return memberships.stream().anyMatch(membership -> membership.getGiver().getUsername().equals(giverUsername));
    }

    public boolean hasMembership(String giverUsername) {
        return memberships.stream().
                anyMatch(membership -> membership.getGiver().getUsername().equals(giverUsername) && membership.isGiverAcceptance());
    }
}
