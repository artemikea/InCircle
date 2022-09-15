package com.incircle.service;

import com.incircle.repo.INotifierRepo;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class IConsoleNotifierService implements INotifierRepo {

    @Override
    public void send(String message) {
        System.out.println("Notification message next: " + message);
    }
}
