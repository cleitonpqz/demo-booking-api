package com.booking.api.service;

import com.booking.api.domain.Block;
import com.booking.api.exceptions.BlockNotFoundException;
import com.booking.api.exceptions.BlockedDateRangeException;
import com.booking.api.exceptions.BookingDateRageException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;
import com.booking.api.repository.BlockDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BlockServiceImpl implements BlockService {

    private BlockDao blockDao;
    private PropertyAvailabilityService propertyAvailabilityService;

    public BlockServiceImpl(BlockDao blockDao, PropertyAvailabilityService propertyAvailabilityService) {
        this.blockDao = blockDao;
        this.propertyAvailabilityService = propertyAvailabilityService;
    }

    public Block create(Block block) throws InvalidPropertyException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException {

        if (block.getFromDate().isAfter(block.getToDate())) {
            throw new InvalidDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBlockedForTheProperty(block.getFromDate(), block.getToDate(), block.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBookedForTheProperty(block.getFromDate(), block.getToDate(), block.getProperty().getId())) {
            throw new BookingDateRageException();
        }

        try {
            return blockDao.save(block);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidPropertyException("Invalid Property Id");
        }
    }

    public Block update(UUID id, LocalDate from, LocalDate to) throws BlockNotFoundException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException {
        Block block = blockDao.findById(id).orElseThrow(() -> new BlockNotFoundException());

        if (block.getFromDate().isAfter(block.getToDate())) {
            throw new InvalidDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBlockedForTheProperty(block.getFromDate(), block.getToDate(), block.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBookedForTheProperty(block.getFromDate(), block.getToDate(), block.getProperty().getId())) {
            throw new BookingDateRageException();
        }

        block.setFromDate(from);
        block.setToDate(to);

        return blockDao.save(block);
    }

    public void delete(UUID id) throws BlockNotFoundException {
        Block block = blockDao.findById(id).orElseThrow(() -> new BlockNotFoundException());
        blockDao.delete(block);
    }

}
