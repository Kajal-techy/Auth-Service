package com.authservice.authservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class JWTExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> NotFoundException(NotFoundException exception) {
        log.info("Entering UserExceptionHandler.NotFoundException with parameter exception {}.", exception);
        return ResponseEntity.badRequest().body(exception.toString());
    }
}

