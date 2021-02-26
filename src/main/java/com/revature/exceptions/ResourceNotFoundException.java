package com.revature.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("No resource(s) found");
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
