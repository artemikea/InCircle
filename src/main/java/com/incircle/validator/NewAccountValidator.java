package com.incircle.validator;

import com.incircle.model.NewAccount;
import com.incircle.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewAccountValidator extends CommonValidator {
    @Autowired
    private IUserRepo userRepo;

    public NewAccountValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, NewAccount.class);
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        NewAccount newAccount = (NewAccount) o;
        String login = newAccount.getUsername().toLowerCase();
        if (userRepo.findByUsername(login) != null) {
            errors.rejectValue("username", "Username exists", "Username exists");
        }
    }
}