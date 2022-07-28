package com.incircle.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class NewContact {

    @NotBlank(message = "{name.empty}")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "{name.regexp}")
    private String name;

    @NotBlank(message = "{phone.empty}")
    @Pattern(regexp = "^(\\+)?((\\d{2,3}) ?\\d|\\d)(([ -]?\\d)|( ?(\\d{2,3}) ?)){5,12}\\d$", message = "{phone.regexp}")
    private String phone;
}
