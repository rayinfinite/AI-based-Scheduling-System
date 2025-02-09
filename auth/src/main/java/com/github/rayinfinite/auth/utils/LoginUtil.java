package com.github.rayinfinite.auth.utils;

import com.github.rayinfinite.auth.config.exception.BusinessException;
import com.github.rayinfinite.auth.entity.User;
import com.github.rayinfinite.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginUtil {
    private final UserRepository userRepository;

    public void checkLogin(String username, String email) {
        if (username == null || email == null) {
            throw new BusinessException("Invalid user information");
        }
        userRepository.findById(email).ifPresentOrElse(user -> {
            if(!user.isActive()){
                throw new BusinessException("User is inactive");
            }
            user.setName(username);
            if (user.getLastLogin() != null) {
                Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
                if (user.getLastLogin().before(tenMinutesAgo)) {
                    user.setLastLogin(new Date());
                    userRepository.save(user);
                }
            } else {
                user.setCreated(new Date());
                user.setLastLogin(new Date());
                userRepository.save(user);
            }
            userRepository.save(user);
        }, () -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(username);
            newUser.setActive(false);
            newUser.setCreated(new Date());
            newUser.setLastLogin(new Date());
            userRepository.save(newUser);
            throw new BusinessException("User not found");
        });
    }
}

