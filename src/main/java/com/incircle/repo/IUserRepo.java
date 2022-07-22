package com.incircle.repo;

import com.incircle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> getUserByUsername(String username);
}
