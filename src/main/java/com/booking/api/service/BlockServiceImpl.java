package com.booking.api.service;

import com.booking.api.domain.Block;
import com.booking.api.exceptions.BlockNotFoundException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;
import com.booking.api.repository.BlockDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BlockServiceImpl implements BlockService {

    private BlockDao blockDao;

    public BlockServiceImpl(BlockDao blockDao) {
        this.blockDao = blockDao;
    }

    public Block create(Block block) throws InvalidPropertyException, InvalidDateRangeException {

        if (block.getFromDate().isAfter(block.getToDate())) {
            throw new InvalidDateRangeException();
        }

        try {
            return blockDao.save(block);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidPropertyException("Invalid Property Id");
        }
    }

    public Block update(UUID id, LocalDate from, LocalDate to) throws BlockNotFoundException, InvalidDateRangeException {
        Block block = blockDao.findById(id).orElseThrow(() -> new BlockNotFoundException());

        if (block.getFromDate().isAfter(block.getToDate())) {
            throw new InvalidDateRangeException();
        }

        block.setFromDate(from);
        block.setToDate(to);

        return blockDao.save(block);
    }

    public void delete(UUID id) throws BlockNotFoundException {
        Block block = blockDao.findById(id).orElseThrow(() -> new BlockNotFoundException());
        blockDao.delete(block);
    }

    public boolean isDateRangeBlockedForTheProperty(LocalDate from, LocalDate to, UUID propertyId) {
        List<Block> blocksByPropertyAndDateRange = blockDao.findBlocksByPropertyAndDateRange(propertyId, from, to);
        return blocksByPropertyAndDateRange.size() > 0;
    }
}
