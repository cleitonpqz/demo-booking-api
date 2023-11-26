package com.booking.api.service;

import java.time.LocalDate;
import java.util.UUID;

public interface PropertyAvailabilityService {
    boolean isDateRangeBlockedForTheProperty(LocalDate from, LocalDate to, UUID propertyId);
    boolean isDateRangeBookedForTheProperty(LocalDate fromDate, LocalDate toDate, UUID propertyId);
    boolean isDateRangeBookedForThePropertyAndOtherBookings(UUID bookingId, LocalDate fromDate, LocalDate toDate, UUID propertyId);
}
