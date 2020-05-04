package com.authservice.authservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class JWTExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> notFoundException(NotFoundException exception) {
        log.info("Entering UserExceptionHandler.notFoundException with parameter exception {}.", exception);
        return ResponseEntity.badRequest().body(exception.toString());
    }

    @ExceptionHandler(value = DependencyFailedException.class)
    public ResponseEntity<String> dependencyFailedException(DependencyFailedException exception) {
        log.info("Entering UserExceptionHandler.dependencyFailedException with parameter exception {}.", exception);
        return ResponseEntity.badRequest().body(exception.toString());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<String> forbiddenException(ForbiddenException exception) {
        log.info("Entering UserExceptionHandler.forbiddenException with parameter exception {}.", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.toString());
    }
}

