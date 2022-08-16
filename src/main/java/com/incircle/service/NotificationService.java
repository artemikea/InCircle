package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.Notification;
import com.incircle.domain.User;
import com.incircle.model.NewNotification;
import com.incircle.repo.INotificationRepo;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private INotificationRepo notificationRepo;

    public List<Notification> getNotifications(User user) {
        return notificationRepo.findByUser(user);
    }

    public Iterable<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    public Either<String, Notification> saveNotification(NewNotification newNotification, User user, Contact contact) {
        Notification notification = new Notification();

        notification.setText(newNotification.getText());
        notification.setDate(newNotification.getDate());
        notification.setContact(contact);
        notification.setUser(user);
//        contact.getNotifications().add(notification);
        return Either.right(notificationRepo.save(notification));
    }

}
