package com.incircle.controllers;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import com.incircle.service.ContactService;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/contacts")
    public String contacts(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("contacts", contactService.getContacts(user));
        return "contacts";
    }

    @GetMapping("/contacts/add")
    public String addContactGet(Model model) {
        model.addAttribute("newContact", new Contact());
        return "addContact";
    }

    @PostMapping("/contacts/add")
    public String addContactPost(@AuthenticationPrincipal User user,
                                 @ModelAttribute("newContact") Contact newContact,
                                 RedirectAttributes redirectAttributes) {
        Either<String, Contact> accountEither = contactService.saveContact(newContact, user);
        if (accountEither.isLeft()) {
            redirectAttributes.addFlashAttribute("message_bad", accountEither.getLeft());
            return "redirect:/contacts/add";
        } else {
            redirectAttributes.addFlashAttribute("message_good", newContact.getContactName() + " created");
            return "redirect:/contacts";
        }
    }
}
