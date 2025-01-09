package com.github.rayinfinite.scheduler.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        log.error("Business Error Handled  ===> ", ex);
        return handleMyException(ex, request, ex.getHttpStatus());
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Object> handleThrowable(Throwable ex, WebRequest request) {
        log.error("System Error Handled  ===> ", ex);
        return handleMyException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RequestRejectedException.class})
    public ResponseEntity<Object> handleRequestRejectedException(RequestRejectedException ex) {
        throw ex;
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        throw ex;
    }

    private ResponseEntity<Object> handleMyException(Throwable ex, WebRequest request, HttpStatus status) {
        ErrorResponseException errorResponseException = new ErrorResponseException(
                status,
                ProblemDetail.forStatusAndDetail(status, ex.getMessage()),
                ex.getCause());
        return handleExceptionInternal(
                errorResponseException,
                null,
                errorResponseException.getHeaders(),
                errorResponseException.getStatusCode(),
                request);
    }
}
