package com.booking.api.service;

import com.booking.api.domain.Block;
import com.booking.api.domain.Booking;
import com.booking.api.repository.BlockDao;
import com.booking.api.repository.BookingDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyAvailabilityServiceImpl implements PropertyAvailabilityService {

    private final BookingDao bookingDao;
    private final BlockDao blockDao;

    public PropertyAvailabilityServiceImpl(BookingDao bookingDao, BlockDao blockDao) {
        this.bookingDao = bookingDao;
        this.blockDao = blockDao;
    }

    @Override
    public boolean isDateRangeBlockedForTheProperty(LocalDate from, LocalDate to, UUID propertyId) {
        List<Block> blocksByPropertyAndDateRange = blockDao.findBlocksByPropertyAndDateRange(propertyId, from, to);
        return blocksByPropertyAndDateRange.size() > 0;
    }

    @Override
    public boolean isDateRangeBookedForTheProperty(LocalDate fromDate, LocalDate toDate, UUID propertyId) {
        List<Booking> bookings = bookingDao.bookingsByPropertyAndDateRange(propertyId, fromDate, toDate);
        return bookings.size() > 0;
    }

    @Override
    public boolean isDateRangeBookedForThePropertyAndOtherBookings(UUID bookingId, LocalDate fromDate, LocalDate toDate, UUID propertyId) {
        List<Booking> bookings = bookingDao.bookingsByPropertyAndDateRangeExcludingCurrentBooking(bookingId, propertyId, fromDate, toDate);
        return bookings.size() > 0;
    }
}
