package com.incircle.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String phone;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Contact() {
    }

    public Contact(String name, String phone, User user) {
        this.name = name;
        this.phone = phone;
        this.user = user;
    }
}