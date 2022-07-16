package com.incircle.controllers;

import com.incircle.domain.User;
import com.incircle.service.UserService;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String addUser(@ModelAttribute("userForm") @Valid User userForm,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "register";
        }
        Either<String, User> accountEither = userService.saveUser(userForm);
        if (accountEither.isLeft()) {
            redirectAttributes.addFlashAttribute("message_bad", accountEither.getLeft());
            return "redirect:/register";
        } else {
            redirectAttributes.addFlashAttribute("message_good", userForm.getUsername() + " created");
            return "redirect:/login";
        }
    }
}
