package com.booking.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Property Identification")
public class InvalidPropertyException extends Exception {

    public InvalidPropertyException(String message) {
        super(message);
    }
}
