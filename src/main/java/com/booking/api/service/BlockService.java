package com.booking.api.service;

import com.booking.api.domain.Block;
import com.booking.api.exceptions.BlockNotFoundException;
import com.booking.api.exceptions.BlockedDateRangeException;
import com.booking.api.exceptions.BookingDateRageException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;

import java.time.LocalDate;
import java.util.UUID;

public interface BlockService {

    Block create(Block block) throws InvalidPropertyException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException;
    Block update(UUID id, LocalDate from, LocalDate to) throws BlockNotFoundException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException;
    void delete(UUID id) throws BlockNotFoundException;

}
