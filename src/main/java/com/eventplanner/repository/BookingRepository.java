package com.eventplanner.repository;

import com.eventplanner.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b join fetch b.event e left join fetch e.theme left join fetch e.user order by b.bookingDate desc")
    List<Booking> findAllForList();

    Optional<Booking> findByEventId(Long eventId);
}
