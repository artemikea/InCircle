package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
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
        return user.getContacts();
    }

    public Either<String, Contact> saveContact(Contact contact, User user) {
        if (contact.getContactName().length() < 1) {
            return Either.left("Contact Name shouldn't be empty!");
        }
        if (contact.getPhone().length() < 1) {
            return Either.left("Phone shouldn't be empty!");
        }
        user.addContact(contact);
        return Either.right(contactRepo.save(contact));
    }
}
