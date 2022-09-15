package com.incircle.repo;

import org.springframework.stereotype.Repository;

@Repository
public interface INotifierRepo {
    void send(String message);
}
