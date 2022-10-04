package com.incircle.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class NewAccount {

    @NotBlank(message = "{username.empty}")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "{username.regexp}")
    private String username;

    @NotBlank(message = "{password.empty}")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "{password.regexp}")
    private String password;
}
