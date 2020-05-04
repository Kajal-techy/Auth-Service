package com.authservice.authservice.exception;

public class Forbidden extends RuntimeException {

    public Forbidden(String message) {
        super(message);
    }
}
