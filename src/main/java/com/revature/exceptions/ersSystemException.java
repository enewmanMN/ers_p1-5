package com.revature.exceptions;

public class ersSystemException extends RuntimeException {

    public ersSystemException(Throwable e) {
        super("An unspecified exception was thrown, see logs for more information", e);
    }

}
