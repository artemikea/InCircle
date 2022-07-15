package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import com.incircle.repo.IContactRepo;
import com.incircle.repo.IUserRepo;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private IContactRepo contactRepo;
    @Autowired
    private IUserRepo userRepo;

    public List<Contact> findByUser(User user) {
        return contactRepo.findByUser(user);
    }

    public Either<String, Contact> saveContact(Contact contact, User user) {
        if (contact.getContactName().length() < 1) {
            return Either.left("Contact Name shouldn't be empty!");
        }
        if (contact.getPhone().length() < 1) {
            return Either.left("Phone shouldn't be empty!");
        }
        contact.setUser(user);
        return Either.right(contactRepo.save(contact));
    }
}
