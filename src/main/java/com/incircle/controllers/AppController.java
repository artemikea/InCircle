package com.incircle.controllers;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import com.incircle.model.NewContact;
import com.incircle.service.ContactService;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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

    @GetMapping(value = "/contacts/delete/{id}")
    public String delContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/contacts";
    }

    @GetMapping("/contacts/{id}")
    public String details(@PathVariable Long id, Model model) {
        //contactService.getContactById(id).ifPresent(o -> model.addAttribute("contact", o));
        model.addAttribute("contact", contactService.getContactById(id).orElse(null));
        return "details";
    }

    @GetMapping("/contacts/add")
    public String addContactGet(Model model) {
        model.addAttribute("newContact", new NewContact());
        return "addContact";
    }

    @PostMapping("/contacts/add")
    public String addContactPost(@AuthenticationPrincipal User user,
                                 @ModelAttribute("newContact") @Valid NewContact newContact,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "addContact";
        }
        Either<String, Contact> accountEither = contactService.saveContact(newContact, user);
        if (accountEither.isLeft()) {
            redirectAttributes.addFlashAttribute("message_bad", accountEither.getLeft());
            return "redirect:/contacts/add";
        } else {
            redirectAttributes.addFlashAttribute("message_good", newContact.getName() + " created");
            return "redirect:/contacts";
        }
    }
}
