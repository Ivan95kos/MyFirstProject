package com.example.MyFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException {

    public MyFileNotFoundException(final String message) {
        super(message);
    }

    public MyFileNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
