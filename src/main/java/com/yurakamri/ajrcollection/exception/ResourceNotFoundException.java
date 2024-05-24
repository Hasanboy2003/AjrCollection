package com.yurakamri.ajrcollection.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super("RESOURCE NOT FOUND!");
    }

    public ResourceNotFoundException(Object id, String name) {
        super(name + " WITH ID: " + id + " NOT FOUND!");
    }
}
