package com.incircle.service;

import com.incircle.domain.Role;
import com.incircle.domain.User;
import com.incircle.repo.IUserRepo;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public Either<String, User> saveUser(User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            return Either.left("User " + user.getUsername() + " already exists");
        }
        if (user.getPassword().length() < 1) {
            return Either.left("Password shouldn't be empty!");
        }
        user.setRoles(Collections.singleton(Role.USER));
        return Either.right(userRepo.save(user));
    }
}