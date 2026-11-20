package com.eventplanner.service;

import com.eventplanner.entity.Booking;
import com.eventplanner.entity.enums.BookingStatus;

import java.util.List;

public interface BookingService {
    List<Booking> findAll();
    Booking requestBooking(Long eventId);
    void updateStatus(Long bookingId, BookingStatus nextStatus);
}
