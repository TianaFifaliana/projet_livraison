package com.example.livraison.exception;

public class DuplicateResourceException extends ConflictException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
