package com.incircle.repo;

import com.incircle.domain.Notification;
import com.incircle.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface INotificationRepo extends CrudRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findNotificationByEndedFalse();
}
