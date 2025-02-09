package com.github.rayinfinite.auth.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@SuppressWarnings("unused")
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1145141919810L;
    private final HttpStatus httpStatus;

    public BusinessException() {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public BusinessException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                             HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }
}
