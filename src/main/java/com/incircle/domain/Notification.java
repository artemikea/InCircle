package com.incircle.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    private Contact contact;

    public Notification() {

    }

    public Notification(String text, Date date, Contact contact) {
        this.text = text;
        this.date = date;
        this.contact = contact;
    }

}
