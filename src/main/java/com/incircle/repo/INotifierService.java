package com.incircle.repo;

import com.incircle.domain.Notification;
import org.springframework.stereotype.Repository;

@Repository
public interface INotifierService {
    void send(Notification notification) throws Exception;
}
