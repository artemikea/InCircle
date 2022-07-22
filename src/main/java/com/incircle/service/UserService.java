package com.incircle.service;

import com.incircle.domain.Role;
import com.incircle.domain.User;
import com.incircle.model.NewAccount;
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
import java.util.Optional;

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

    public Optional<User> getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    public Either<String, User> saveUser(NewAccount newAccount) {
        User user = new User();

        user.setUsername(newAccount.getUsername());
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(newAccount.getPassword()));
        return Either.right(userRepo.save(user));
    }

}