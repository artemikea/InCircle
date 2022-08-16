package com.incircle.service;

import com.incircle.repo.INotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class SchedulingNotifications {

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedDelay = 10000)
    public void cicling() {
        notificationService.getAllNotifications();
    }
}
