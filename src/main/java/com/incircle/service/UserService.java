package com.incircle.service;

import com.incircle.domain.Role;
import com.incircle.domain.User;
import com.incircle.repo.IUserRepo;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepo userRepo;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public Either<String, User> saveUser(User user) {
        String login = user.getUsername().toLowerCase();
        if (userRepo.findByUsername(login) != null) {
            return Either.left("User " + user.getUsername() + " already exists");
        }

        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Either.right(userRepo.save(user));
    }

}