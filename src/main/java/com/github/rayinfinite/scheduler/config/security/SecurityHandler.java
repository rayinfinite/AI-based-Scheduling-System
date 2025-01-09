package com.github.rayinfinite.scheduler.config.security;

import com.github.rayinfinite.scheduler.entity.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecurityHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        writeResponse(response, HttpStatus.UNAUTHORIZED, "Authentication Failed");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        writeResponse(response, HttpStatus.FORBIDDEN, "Access Denied");
    }

    private void writeResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        PrintWriter writer = response.getWriter();
        writer.print(Response.builder().message(message).code("fail").build());
        writer.flush();
        writer.close();
    }
}
