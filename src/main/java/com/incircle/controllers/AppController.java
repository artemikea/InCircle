package com.incircle.controllers;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import com.incircle.repo.IContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AppController {
    @Autowired
    private IContactRepo contactRepo;
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/contacts")
    public String contacts(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("contacts", contactRepo.findByUser(user));
        return "contacts";
    }
    @PostMapping("/contacts")
    public String addContact(@AuthenticationPrincipal User user, String contactName, String phone, Model model) {
        Contact contact = new Contact(contactName, phone, user);
        contactRepo.save(contact);
        model.addAttribute("contacts", contactRepo.findByUser(user));
        return "contacts";
    }
}
