package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import com.incircle.repo.IContactRepo;
import com.incircle.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private IContactRepo contactRepo;

    public Iterable<Contact> contacts(User user) {
        return contactRepo.findByUser(user);
    }
}
