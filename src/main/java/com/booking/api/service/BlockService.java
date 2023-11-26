package com.booking.api.service;

import com.booking.api.domain.Block;
import com.booking.api.exceptions.BlockNotFoundException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;

import java.time.LocalDate;
import java.util.UUID;

public interface BlockService {

    Block create(Block block) throws InvalidPropertyException, InvalidDateRangeException;
    Block update(UUID id, LocalDate from, LocalDate to) throws BlockNotFoundException, InvalidDateRangeException;
    void delete(UUID id) throws BlockNotFoundException;
    boolean isDateRangeBlockedForTheProperty(LocalDate from, LocalDate to, UUID propertyId);

}
