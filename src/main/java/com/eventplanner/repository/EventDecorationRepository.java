package com.eventplanner.repository;

import com.eventplanner.entity.EventDecoration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventDecorationRepository extends JpaRepository<EventDecoration, Long> {
    Optional<EventDecoration> findByEventIdAndItemId(Long eventId, Long itemId);
    Optional<EventDecoration> findByIdAndEventId(Long id, Long eventId);
}
