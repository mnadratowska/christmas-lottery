package com.javagda24.christmaslottery.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String description;

    @CreationTimestamp
    private LocalDateTime additionDate;

    private String link;

    private Integer price;

    @ManyToOne
    private Account account;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "gift", orphanRemoval = true)
    private Set<Comment> comments;

    public String getLinkAdress() {
        if (link.startsWith("http")) {
            return link;
        } else {
            return "http://" + link;
        }
    }

    public String additionDateToString() {
        return additionDate.toLocalDate().toString() + " " + additionDate.toLocalTime().toString();
    }

    public String getLastComment() {
        if (comments.isEmpty()) {
            return "";
        }
        return comments.stream().sorted((o1, o2) -> timeComparator(o1, o2)).collect(Collectors.toList()).get(0).getContent();
    }

    private int timeComparator(Comment c1, Comment c2) {
        if (c1.getAdditionTime().isBefore(c2.getAdditionTime())) return 1;
        else return -1;
    }
}
