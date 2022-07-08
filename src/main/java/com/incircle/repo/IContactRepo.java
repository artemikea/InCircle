package com.incircle.repo;

import com.incircle.domain.Contact;
import com.incircle.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IContactRepo extends CrudRepository<Contact, Long> {

    List<Contact> findByUser(User user);

}
