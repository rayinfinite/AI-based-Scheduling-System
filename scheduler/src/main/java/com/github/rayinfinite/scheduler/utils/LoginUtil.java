package com.github.rayinfinite.scheduler.utils;

import com.github.rayinfinite.scheduler.config.security.UserInfoDetails;
import com.github.rayinfinite.scheduler.entity.AuditLog;
import com.github.rayinfinite.scheduler.repository.AuditLogRepository;
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
