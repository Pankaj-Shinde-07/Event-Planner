package com.eventplanner.service.impl;

import com.eventplanner.entity.Booking;
import com.eventplanner.entity.Event;
import com.eventplanner.entity.EventDecoration;
import com.eventplanner.entity.enums.BookingStatus;
import com.eventplanner.entity.enums.EventStatus;
import com.eventplanner.exception.ResourceNotFoundException;
import com.eventplanner.repository.BookingRepository;
import com.eventplanner.repository.EventRepository;
import com.eventplanner.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, EventRepository eventRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findAll() {
        return bookingRepository.findAllForList();
    }

    @Override
    public Booking requestBooking(Long eventId) {
        Event event = eventRepository.findWithDetailsById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("That event could not be found."));
        if (event.getBooking() != null || bookingRepository.findByEventId(eventId).isPresent()) {
            throw new IllegalStateException("A booking request already exists for this event.");
        }
        if (event.getDecorations().isEmpty()) {
            throw new IllegalArgumentException("Add at least one decoration item before requesting a booking.");
        }
        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalStateException("This event is not open for a booking request.");
        }

        BigDecimal total = event.getDecorations().stream()
                .map(EventDecoration::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Booking booking = new Booking(event, total);
        event.setBooking(booking);
        event.setStatus(EventStatus.REQUESTED);
        return bookingRepository.save(booking);
    }

    @Override
    public void updateStatus(Long bookingId, BookingStatus nextStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("That booking could not be found."));
        BookingStatus current = booking.getBookingStatus();
        if (!isAllowedTransition(current, nextStatus)) {
            throw new IllegalArgumentException("That booking status change is not allowed.");
        }

        booking.setBookingStatus(nextStatus);
        Event event = booking.getEvent();
        event.setStatus(switch (nextStatus) {
            case CONFIRMED -> EventStatus.BOOKED;
            case IN_PROGRESS -> EventStatus.IN_PROGRESS;
            case COMPLETED -> EventStatus.COMPLETED;
            case CANCELLED -> EventStatus.CANCELLED;
            case PENDING -> EventStatus.REQUESTED;
        });
        bookingRepository.save(booking);
    }

    private boolean isAllowedTransition(BookingStatus current, BookingStatus next) {
        return switch (current) {
            case PENDING -> next == BookingStatus.CONFIRMED || next == BookingStatus.CANCELLED;
            case CONFIRMED -> next == BookingStatus.IN_PROGRESS || next == BookingStatus.CANCELLED;
            case IN_PROGRESS -> next == BookingStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
