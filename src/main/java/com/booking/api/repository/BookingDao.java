package com.booking.api.repository;

import com.booking.api.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingDao extends JpaRepository<Booking, UUID> {

    @Query("" +
            "select b " +
            "from Booking b " +
            "where b.property.id = :propertyId " +
            "and b.fromDate <= :toDate " +
            "and b.toDate >= :fromDate " +
            "and b.isCanceled = false ")
    List<Booking> bookingsByPropertyAndDateRange(UUID propertyId, LocalDate fromDate, LocalDate toDate);

    @Query("" +
            "select b " +
            "from Booking b " +
            "where b.property.id = :propertyId " +
            "and b.id <> :id " +
            "and b.fromDate <= :toDate " +
            "and b.toDate >= :fromDate " +
            "and b.isCanceled = false ")
    List<Booking> bookingsByPropertyAndDateRangeExcludingCurrentBooking(UUID id, UUID propertyId, LocalDate fromDate, LocalDate toDate);

}
