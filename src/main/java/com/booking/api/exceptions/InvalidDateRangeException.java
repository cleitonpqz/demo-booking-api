package com.booking.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "'from' date is after the 'to' date")
public class InvalidDateRangeException extends Exception {
}
