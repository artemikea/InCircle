package com.incircle.service;

import com.incircle.domain.Contact;
import com.incircle.domain.Notification;
import com.incircle.domain.NotificationType;
import com.incircle.domain.User;
import com.incircle.model.NewNotification;
import com.incircle.repo.INotificationRepo;
import com.incircle.repo.INotifierService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {
    @Autowired
    private INotificationRepo notificationRepo;
    @Autowired
    private INotifierService notifierService;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    List<Future<List<Notification>>> currentFutureList = new LinkedList<>();

    public List<Notification> getNotifications(User user) {
        return notificationRepo.findByUser(user);
    }

    List<Notification> getNotificationList() {
        return Streamable
                .of(notificationRepo.findByType(NotificationType.NO_FINISHED))
                .toList();
    }

    List<String> getPhoneNumberList(List<Notification> notificationList) {
        return notificationList.stream().map((notification) -> {
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
        notification.setType(NotificationType.FINISHED);
        notificationRepo.save(notification);
    }

    Optional<Notification> processingSendNotificationToZvonok(Notification notification) {
        try {
            notifierService.send(notification);
            return Optional.empty();
        } catch (HttpClientErrorException e) {
//            System.out.println(e.getRawStatusCode());
            log.error("", e);
            int code = e.getRawStatusCode();
            if (code == 400) {
                return Optional.of(notification);
            } else if (code == 429) {
                makeThreadSleep();
                return Optional.of(notification);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    List<Notification> getDelayedNotification(List<Notification> notificationList) {
        return notificationList.stream()
                .map(notification -> {
                    Time time;
                    if (notification.getDelay().getTime() > 300000) {
                        notification.setType(NotificationType.FAILED);
                        return notification;
                    } else if (notification.getDelay().getTime() != 0) {
                        time = new Time(notification.getDelay().getTime() * 6);
                    } else {
                        time = new Time(6000);
                    }
                    notification.setDelay(time);
                    notification.setLastChanged(LocalDateTime.now());
                    notification.setType(NotificationType.DELAYED);
                    return notification;
                }).collect(Collectors.toList());
    }
    List<Notification> getFailedNotification(List<Notification> notificationList) {
        return notificationList.stream()
                .filter(notification -> notification.getType() == NotificationType.FAILED)
                .collect(Collectors.toList());
    }

    void completeFailedTasts() {
        currentFutureList.forEach((future) -> {
            if (future.isDone()) {
                try {
                    var notificationList = future.get();
                    if (notificationList.size() > 0) {
                        var delayedList = getDelayedNotification(notificationList);
                        var failedNotification = getFailedNotification(delayedList);
                        notificationRepo.saveAll(failedNotification);
                        delayedList.removeAll(failedNotification);
                        notificationRepo.saveAll(delayedList);
                        NotificationCallable callable =
                                createThread(delayedList.stream()
                                        .findFirst()
                                        .get()
                                        .getContact()
                                        .getPhone(), delayedList);
                        FutureTask<List<Notification>> futureTask
                                = new FutureTask<>(callable);
                        Future<List<Notification>> result
                                = (Future<List<Notification>>) executor.submit(futureTask);
                        currentFutureList.add(result);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    NotificationCallable createThread(String key, List<Notification> value) {
        Date currentDate = new Date();
        return new NotificationCallable(key, value, (notification, failedNotifications) -> {
            LocalDateTime currentLocalDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                            ZoneId.systemDefault());
            if (notification.getDate().isAfter(currentLocalDateTime)) {
                processingSendNotificationToZvonok(notification)
                        .ifPresent(failedNotifications::add);
            } else {
                saveNotification(notification);
            }
        });
    }

    Map<String, List<Notification>> prepareData(List<String> phoneList, List<Notification> notificationList) {
        Map<String, List<Notification>> map = new ConcurrentHashMap<>();
        phoneList.forEach((phoneNumber) -> {
            List<Notification> valueList = notificationList.stream()
                    .filter(notification -> notification.getContact().getPhone().equals(phoneNumber))
                    .collect(Collectors.toList());
            map.put(phoneNumber, valueList);
        });
        return map;
    }

    @Scheduled(fixedRate = 6000)
    public void scheduleNotificationByCurrentDate() {
        completeFailedTasts();
        List<Notification> notificationList = getNotificationList();
        List<String> phoneList = getPhoneNumberList(notificationList);
        Map<String, List<Notification>> map = prepareData(phoneList, notificationList);
        map.forEach((key, value) -> {
            NotificationCallable callable = createThread(key, value);
            FutureTask<List<Notification>> future = new FutureTask<>(callable);
            Future<List<Notification>> result = (Future<List<Notification>>) executor.submit(future);
            currentFutureList.add(result);
        });
    }

    public Either<String, Notification> saveNotification(NewNotification newNotification, User user, Contact contact) {
        Notification notification = new Notification();

        notification.setText(newNotification.getText());
        notification.setDate(newNotification.getDate());
        notification.setContact(contact);
        notification.setUser(user);
        return Either.right(notificationRepo.save(notification));
    }
}
