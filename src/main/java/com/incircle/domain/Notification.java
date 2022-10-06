package com.incircle.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @Column(length = 32, columnDefinition = "varchar(32) default 'NO_FINISHED'")
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastChanged;

    private Time delay;

    @ManyToOne(fetch = FetchType.EAGER)
    private Contact contact;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Notification() {

    }
}
