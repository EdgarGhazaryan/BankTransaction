package com.bank.BankTransaction.service;

import com.bank.BankTransaction.model.User;
import com.bank.BankTransaction.repository.UserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String hashPassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        }
        return generatedPassword;
    }

    public Pair<String, Boolean> validate(User user) {
        Pair<String, Boolean> pair;
        if(user.getFirstName() == null || user.getFirstName().length() == 0) {
            pair = Pair.of("Invalid first name", false);
        } else if(user.getLastName() == null || user.getLastName().length() == 0) {
            pair = Pair.of("Invalid last name", false);
        } else if(user.getEmail() == null || !user.getEmail().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            pair = Pair.of("Invalid email", false);
        } else if(user.getPassword() == null || user.getPassword().length() < 6) {
            pair = Pair.of("Invalid password. Password must contain minimum 6 symbols.", false);
        } else {
            pair = Pair.of("You are successfully registered", true);
        }
        return pair;
    }

    public void save(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        userRepository.save(user);
    }

    public User getById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public void update(User user) {
        userRepository.save(user);
    }
}
