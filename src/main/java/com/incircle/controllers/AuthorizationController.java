package com.incircle.controllers;

import com.incircle.domain.Role;
import com.incircle.domain.User;
import com.incircle.repo.IUserRepo;
import com.incircle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class AuthorizationController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userForm", new User());
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute("userForm") User userForm, Model model) {

        if(!userService.saveUser(userForm)){
            model.addAttribute("message", "Пользователь с таким именем уже существует");
            return "register";
        }

        return "redirect:/login";
    }
}
