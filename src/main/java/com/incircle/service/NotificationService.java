package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.Notification;
import com.incircle.domain.User;
import com.incircle.model.NewNotification;
import com.incircle.repo.INotificationRepo;
import com.incircle.repo.INotifierRepo;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private INotificationRepo notificationRepo;
    @Autowired
    private INotifierRepo notifierRepo;

    public List<Notification> getNotifications(User user) {
        return notificationRepo.findByUser(user);
    }

    @Scheduled(fixedRate = 1000)
    public void sortNotificationByCurrentDate(@AuthenticationPrincipal User user) {
        List<Notification> notificationList = getNotifications(user);
        Date currentDate = new Date();
        LocalDateTime currentLocalDateTime =
                LocalDateTime.ofInstant(currentDate.toInstant(),
                                        ZoneId.systemDefault());
        notificationList.forEach( (notification) -> {
            if (notification.getDate().isAfter(currentLocalDateTime)) {
                notifierRepo.send(notification.getText());
            }
            notification.setEnded(true);
            notificationRepo.save(notification);
        });
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
