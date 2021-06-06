package com.javi.uned.pfgbackend.domain.exceptions;

public class ExistingUserException extends Exception {

    public ExistingUserException() {
    }

    public ExistingUserException(String message) {
        super(message);
    }

    public ExistingUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingUserException(Throwable cause) {
        super(cause);
    }

}
