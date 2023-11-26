package com.booking.api.service;

import com.booking.api.domain.Booking;
import com.booking.api.exceptions.BlockedDateRangeException;
import com.booking.api.exceptions.BookingDateRageException;
import com.booking.api.exceptions.BookingNotFoundException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;

import java.time.LocalDate;
import java.util.UUID;

public interface BookingService {

    Booking save(Booking booking) throws InvalidPropertyException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException;
    Booking findById(UUID id) throws BookingNotFoundException;
    Booking update(UUID id, LocalDate from, LocalDate to) throws BookingNotFoundException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException;
    void delete(UUID id) throws BookingNotFoundException;
    void cancel(UUID id) throws BookingNotFoundException;
    void rebook(UUID id) throws BookingNotFoundException, BlockedDateRangeException, BookingDateRageException;

}
