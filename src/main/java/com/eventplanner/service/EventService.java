package com.eventplanner.service;

import com.eventplanner.dto.EventForm;
import com.eventplanner.entity.Event;

import java.util.List;

public interface EventService {
    List<Event> findAll();
    Event findWithDetails(Long id);
    Event create(EventForm form);
    Event update(Long id, EventForm form);
    void delete(Long id);
    void addDecoration(Long eventId, Long itemId, int quantity);
    void removeDecoration(Long eventId, Long decorationId);
}
