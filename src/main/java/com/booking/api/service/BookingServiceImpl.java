package com.booking.api.service;

import com.booking.api.domain.Booking;
import com.booking.api.exceptions.BlockedDateRangeException;
import com.booking.api.exceptions.BookingDateRageException;
import com.booking.api.exceptions.BookingNotFoundException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;
import com.booking.api.repository.BookingDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final BlockService blockService;

    public BookingServiceImpl(BookingDao bookingDao, BlockService blockService) {
        this.bookingDao = bookingDao;
        this.blockService = blockService;
    }

    public Booking save(Booking booking) throws InvalidPropertyException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException {

        if (booking.getFromDate().isAfter(booking.getToDate())) {
            throw new InvalidDateRangeException();
        }

        if (blockService.isDateRangeBlockedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (isDateRangeUsedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BookingDateRageException();
        }

        try {
            return bookingDao.save(booking);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidPropertyException("Invalid Property Id");
        }
    }

    public Booking findById(UUID id) throws BookingNotFoundException {
        return bookingDao.findById(id).orElseThrow(() -> new BookingNotFoundException());
    }

    public Booking update(UUID id, LocalDate from, LocalDate to) throws BookingNotFoundException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException {
        Booking booking = bookingDao.findById(id).orElseThrow(() -> new BookingNotFoundException());

        if (from.isAfter(to)) {
            throw new InvalidDateRangeException();
        }

        if (blockService.isDateRangeBlockedForTheProperty(from, to, booking.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (isDateRangeUsedForThePropertyAndOtherBookings(id, from, to, booking.getProperty().getId())) {
            throw new BookingDateRageException();
        }

        booking.setFromDate(from);
        booking.setToDate(to);

        return bookingDao.save(booking);
    }

    public void delete(UUID id) throws BookingNotFoundException {
        Booking booking = bookingDao.findById(id).orElseThrow(() -> new BookingNotFoundException());
        bookingDao.delete(booking);
    }

    public void cancel(UUID id) throws BookingNotFoundException {
        Booking booking = bookingDao.findById(id).orElseThrow(() -> new BookingNotFoundException());
        booking.setIsCanceled(true);
        bookingDao.save(booking);
    }

    public void rebook(UUID id) throws BookingNotFoundException, BlockedDateRangeException, BookingDateRageException {
        Booking booking = bookingDao.findById(id).orElseThrow(() -> new BookingNotFoundException());

        if (blockService.isDateRangeBlockedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (isDateRangeUsedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BookingDateRageException();
        }

        booking.setIsCanceled(false);
        bookingDao.save(booking);
    }

    private boolean isDateRangeUsedForTheProperty(LocalDate fromDate, LocalDate toDate, UUID propertyId) {
        List<Booking> bookings = bookingDao.bookingsByPropertyAndDateRange(propertyId, fromDate, toDate);
        return bookings.size() > 0;
    }

    private boolean isDateRangeUsedForThePropertyAndOtherBookings(UUID bookingId, LocalDate fromDate, LocalDate toDate, UUID propertyId) {
        List<Booking> bookings = bookingDao.bookingsByPropertyAndDateRangeExcludingCurrentBooking(bookingId, propertyId, fromDate, toDate);
        return bookings.size() > 0;
    }
}
