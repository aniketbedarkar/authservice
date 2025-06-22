package com.nest.authservice.exception;

public class NoChangesDetectedException extends RuntimeException {
    public NoChangesDetectedException(String message) {
        super(message);
    }
}
