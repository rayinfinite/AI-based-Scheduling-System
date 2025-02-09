package com.github.rayinfinite.algorithm.utils;

import com.github.rayinfinite.algorithm.config.security.UserInfoDetails;
import com.github.rayinfinite.algorithm.entity.AuditLog;
import com.github.rayinfinite.algorithm.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoginUtil {
    private final AuditLogRepository repository;

    public void log(String url, String params, UserInfoDetails principal) {
        AuditLog log = new AuditLog();
        log.setPath(url);
        log.setParams(params);
        log.setUsername(principal.getUsername());
        log.setEmail(principal.getEmail());
        log.setTimestamp(new Date());
        repository.save(log);
    }
}
