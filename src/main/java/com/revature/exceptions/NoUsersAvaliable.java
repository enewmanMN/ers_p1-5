package com.revature.exceptions;

public class NoUsersAvaliable extends RuntimeException {

    public NoUsersAvaliable() {
        super("No users in database!");
    }

    public NoUsersAvaliable(String message) {
        super(message);
    }

}
