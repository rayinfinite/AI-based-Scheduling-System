package com.github.rayinfinite.scheduler.utils;

import com.github.rayinfinite.scheduler.config.exception.BusinessException;
import com.github.rayinfinite.scheduler.entity.AuditLog;
import com.github.rayinfinite.scheduler.entity.User;
import com.github.rayinfinite.scheduler.repository.AuditLogRepository;
import com.github.rayinfinite.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginUtil {
    private final AuditLogRepository repository;
    private final UserRepository userRepository;

    public void log(String url, String params, OidcUser principal) {
        AuditLog log = new AuditLog();
        log.setPath(url);
        log.setParams(params);
        Map<String, Object> claim = principal.getIdToken().getClaims();
        if (claim.containsKey("name")) {
            log.setUsername(claim.get("name").toString());
        }
        if (claim.containsKey("preferred_username")) {
            log.setEmail(claim.get("preferred_username").toString());
        }
        log.setTimestamp(new Date());
        checkLogin(log.getUsername(), log.getEmail());
        repository.save(log);
    }

    public void checkLogin(String username, String email) {
        if (username == null || email == null) {
            throw new BusinessException("Invalid user information");
        }
        userRepository.findById(email).ifPresentOrElse(user -> {
            if(!user.isActive()){
                throw new BusinessException("User is inactive");
            }
            if (user.getLastLogin() != null) {
                Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
                if (user.getLastLogin().before(tenMinutesAgo)) {
                    user.setLastLogin(new Date());
                }
            } else {
                user.setLastLogin(new Date());
            }
            userRepository.save(user);
        }, () -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(username);
            newUser.setActive(true);
            newUser.setCreated(new Date());
            newUser.setLastLogin(new Date());
            userRepository.save(newUser);
        });
    }
}
