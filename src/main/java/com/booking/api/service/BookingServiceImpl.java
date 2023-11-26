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
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final PropertyAvailabilityService propertyAvailabilityService;

    public BookingServiceImpl(BookingDao bookingDao, PropertyAvailabilityService propertyAvailabilityService) {
        this.bookingDao = bookingDao;
        this.propertyAvailabilityService = propertyAvailabilityService;
    }

    public Booking save(Booking booking) throws InvalidPropertyException, InvalidDateRangeException, BlockedDateRangeException, BookingDateRageException {

        if (booking.getFromDate().isAfter(booking.getToDate())) {
            throw new InvalidDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBlockedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBookedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
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

        if (propertyAvailabilityService.isDateRangeBlockedForTheProperty(from, to, booking.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBookedForThePropertyAndOtherBookings(id, from, to, booking.getProperty().getId())) {
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

        if (propertyAvailabilityService.isDateRangeBlockedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BlockedDateRangeException();
        }

        if (propertyAvailabilityService.isDateRangeBookedForTheProperty(booking.getFromDate(), booking.getToDate(), booking.getProperty().getId())) {
            throw new BookingDateRageException();
        }

        booking.setIsCanceled(false);
        bookingDao.save(booking);
    }

}
