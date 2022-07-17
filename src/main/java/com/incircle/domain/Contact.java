package com.incircle.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "{name.empty}")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "{name.regexp}")
    private String name;
    @NotBlank(message = "{phone.empty}")
    @Pattern(regexp = "^(\\+)?((\\d{2,3}) ?\\d|\\d)(([ -]?\\d)|( ?(\\d{2,3}) ?)){5,12}\\d$", message = "{phone.regexp}")
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