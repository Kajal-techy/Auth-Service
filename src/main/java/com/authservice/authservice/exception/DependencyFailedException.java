package com.authservice.authservice.exception;

public class DependencyFailedException extends RuntimeException {

    public DependencyFailedException(String message) {
        super(message);
    }
}
