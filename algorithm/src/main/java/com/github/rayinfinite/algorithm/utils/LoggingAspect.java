package com.github.rayinfinite.algorithm.utils;

import com.github.rayinfinite.algorithm.config.security.UserInfoDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final LoginUtil loginUtil;

    @Around("@annotation(logAction)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogAction logAction) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String actionDescription = logAction.value();

        if (actionDescription.isBlank()) {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            String method = request.getMethod();
            actionDescription = method + " " + uri;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserInfoDetails oidcUser) {
            if (args.length > 0) { // 确保至少有一个参数用于toString
                String params = switch (args[0]) {
                    case String s -> s;
                    case File file -> file.getAbsolutePath();
                    case MultipartFile file -> file.getOriginalFilename();
                    case null -> null;
                    default -> args[0].toString();
                };
                loginUtil.log(actionDescription, params, oidcUser); // 假设第一个参数是要记录的对象
            }

            return joinPoint.proceed();
        } else {
            throw new IllegalStateException("Not Authorized");
        }
    }
}
