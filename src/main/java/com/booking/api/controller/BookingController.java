package com.booking.api.controller;

import com.booking.api.domain.Booking;
import com.booking.api.exceptions.BlockedDateRangeException;
import com.booking.api.exceptions.BookingDateRageException;
import com.booking.api.exceptions.BookingNotFoundException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;
import com.booking.api.service.BookingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable UUID id) throws BookingNotFoundException {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBooking(@RequestBody Booking booking) throws InvalidPropertyException, URISyntaxException, InvalidDateRangeException, BookingDateRageException, BlockedDateRangeException {
        Booking savedBooking = bookingService.save(booking);
        URI location = new URI("/bookings/" + savedBooking.getId());
        return ResponseEntity.created(location).body(savedBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody Booking booking) throws InvalidDateRangeException, BookingNotFoundException, BlockedDateRangeException, BookingDateRageException {
        return ResponseEntity.ok(bookingService.update(id, booking.getFromDate(), booking.getToDate()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) throws BookingNotFoundException {
        bookingService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity cancel(@PathVariable UUID id) throws BookingNotFoundException {
        bookingService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/rebook")
    public ResponseEntity rebook(@PathVariable UUID id) throws BookingNotFoundException, BlockedDateRangeException, BookingDateRageException {
        bookingService.rebook(id);
        return ResponseEntity.noContent().build();
    }
}
