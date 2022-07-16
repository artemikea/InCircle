package com.incircle.repo;

import com.incircle.domain.Contact;
import org.springframework.data.repository.CrudRepository;

public interface IContactRepo extends CrudRepository<Contact, Long> {

}
