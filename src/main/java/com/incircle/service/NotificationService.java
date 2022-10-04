package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.Notification;
import com.incircle.domain.User;
import com.incircle.model.NewNotification;
import com.incircle.repo.INotificationRepo;
import com.incircle.repo.INotifierService;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
public class NotificationService {
    @Autowired
    private INotificationRepo notificationRepo;
    @Autowired
    private INotifierService notifierService;

//    static String currentPhoneNumber;

    ExecutorService executor = Executors.newFixedThreadPool(10);

//    List<Notification> notSavedNotificationList = new ArrayList<>();
    List<Future<List<Notification>>> currentFutureList = new LinkedList<>();

    public List<Notification> getNotifications(User user) {
        return notificationRepo.findByUser(user);
    }

    List<Notification> getNotificationList() {
        return Streamable
                        .of(notificationRepo.findNotificationByEndedFalse())
                        .toList();
    }

    List<String> getPhoneNumberList(List<Notification> notificationList) {
        return notificationList.stream().map( (notification) -> {
            return notification.getContact().getPhone();
        }).collect(Collectors.toList());
    }

    void makeThreadSleep() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }

    void saveNotification(Notification notification) {
        notification.setEnded(true);
        try {
            notificationRepo.save(notification);
        } catch (RuntimeException e) {
            notSavedNotificationList.add(notification);
        }
    }
    Optional<Notification> processingSendNotificationToZvonok(Notification notification) {
        try {
            notifierService.send(notification);
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getRawStatusCode());
            int code = e.getRawStatusCode();
            if (code == 400) {
                return Optional.of(notification);
            } else if (code == 429) {
                makeThreadSleep();
                return Optional.of(notification);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Scheduled(fixedRate = 6000)
    public void scheduleNotificationByCurrentDate() {
        List<Notification> notificationList = getNotificationList();
        List<String> phoneList = getPhoneNumberList(notificationList);

        Date currentDate = new Date();

        Map<String, List<Notification>> map = new ConcurrentHashMap<>();

        phoneList.forEach((phoneNumber)-> {
            List<Notification> valueList = notificationList.stream()
                    .filter(notification -> notification.getContact().getPhone().equals(phoneNumber))
                    .collect(Collectors.toList());
            map.put(phoneNumber, valueList);
        });
            map.forEach((key, value) -> {
                NotificationCallable callable =
                        new NotificationCallable(key, value, (notification, failedNotifications) -> {
                            LocalDateTime currentLocalDateTime =
                                    LocalDateTime.ofInstant(currentDate.toInstant(),
                                            ZoneId.systemDefault());
                            if (notification.getDate().isAfter(currentLocalDateTime)) {

                                processingSendNotificationToZvonok(notification).ifPresent(failedNotifications::add);
                            } else {
                                saveNotification(notification);
                            }
                        });
                FutureTask<List<Notification>> future = new FutureTask<>(callable);
                Future<List<Notification>> result = (Future<List<Notification>>) executor.submit(future);
                currentFutureList.add(result);
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
