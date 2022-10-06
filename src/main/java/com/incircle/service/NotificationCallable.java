package com.incircle.service;

import com.incircle.domain.Notification;
import groovy.cli.OptionField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
@RequiredArgsConstructor
public class NotificationCallable implements Callable<List<Notification>> {

    final String phoneNumber;
    final List<Notification> notificationList;

    private List<Notification> failedNotifications;
    final BiConsumer<Notification, List<Notification>> callBack;

    @Override
    public List<Notification> call() throws Exception {
        notificationList.forEach( (notification) -> {
            callBack.accept(notification, failedNotifications);
        });
        return failedNotifications;
    }
}
