package com.revature.exceptions;

public class UserNotRegistered extends RuntimeException {

    public UserNotRegistered() {
        super("User not avaliable!");
    }

    public UserNotRegistered(String message) {
        super(message);
    }

}
