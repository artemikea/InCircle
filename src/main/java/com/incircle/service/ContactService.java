package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import com.incircle.model.NewContact;
import com.incircle.repo.IContactRepo;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private IContactRepo contactRepo;

    public List<Contact> getContacts(User user) {
        return contactRepo.findByUser(user);
    }

    public void deleteContact(Long id) {
        contactRepo.deleteById(id);
    }

    public Either<String, Contact> saveContact(NewContact newContact, User user) {
        Contact contact = new Contact();

        contact.setName(newContact.getName());
        contact.setPhone(newContact.getPhone());
        contact.setUser(user);
        user.getContacts().add(contact);
        return Either.right(contactRepo.save(contact));
    }
}
