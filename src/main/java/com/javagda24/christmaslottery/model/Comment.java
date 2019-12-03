package com.javagda24.christmaslottery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(500)")
    private String content;

    @CreationTimestamp
    private LocalDateTime additionTime;

    @ManyToOne
    private Gift gift;

    @ManyToOne
    private Account account;

    public String additionTimeToString() {
        return additionTime.toLocalDate().toString() + " " + additionTime.toLocalTime().toString();
    }
}
