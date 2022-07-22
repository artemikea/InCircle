package com.incircle.controllers;

import com.incircle.domain.User;
import com.incircle.model.NewAccount;
import com.incircle.service.UserService;
import com.incircle.validator.NewAccountValidator;
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
    private NewAccountValidator newAccountValidator;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("newAccount", new NewAccount());
        return "register";
    }

    @PostMapping("/register")
    public String addUser(Model model, RedirectAttributes redirectAttributes,
                          @ModelAttribute("newAccount") @Valid NewAccount newAccount,
                          BindingResult bindingResult) {
        newAccountValidator.validate(newAccount, bindingResult);
        if(bindingResult.hasErrors()){
            return "register";
        }
        Either<String, User> accountEither = userService.saveUser(newAccount);
        if (accountEither.isLeft()) {
            redirectAttributes.addFlashAttribute("message_bad", accountEither.getLeft());
            return "redirect:/register";
        } else {
            redirectAttributes.addFlashAttribute("message_good", newAccount.getUsername() + " created");
            return "redirect:/login";
        }
    }
}
