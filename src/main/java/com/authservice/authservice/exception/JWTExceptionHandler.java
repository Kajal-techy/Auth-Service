package com.authservice.authservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class JWTExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(JWTExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> userNotFoundException(NotFoundException exception) {
        logger.debug("Entering UserExceptionHandler.userNotFoundException with parameter exception {}.", exception);
        return ResponseEntity.badRequest().body(exception.toString());
    }
}

