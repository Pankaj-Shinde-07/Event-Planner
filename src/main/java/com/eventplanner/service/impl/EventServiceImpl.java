package com.eventplanner.service.impl;

import com.eventplanner.dto.EventForm;
import com.eventplanner.entity.DecorationItem;
import com.eventplanner.entity.Event;
import com.eventplanner.entity.EventDecoration;
import com.eventplanner.entity.Theme;
import com.eventplanner.entity.User;
import com.eventplanner.entity.enums.EventStatus;
import com.eventplanner.exception.ResourceNotFoundException;
import com.eventplanner.repository.DecorationItemRepository;
import com.eventplanner.repository.EventDecorationRepository;
import com.eventplanner.repository.EventRepository;
import com.eventplanner.repository.ThemeRepository;
import com.eventplanner.repository.UserRepository;
import com.eventplanner.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final DecorationItemRepository decorationItemRepository;
    private final EventDecorationRepository eventDecorationRepository;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            ThemeRepository themeRepository, DecorationItemRepository decorationItemRepository,
                            EventDecorationRepository eventDecorationRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.decorationItemRepository = decorationItemRepository;
        this.eventDecorationRepository = eventDecorationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepository.findAllForList();
    }

    @Override
    @Transactional(readOnly = true)
    public Event findWithDetails(Long id) {
        return eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("That event could not be found."));
    }

    @Override
    public Event create(EventForm form) {
        User owner = userRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("The demo customer has not been initialized."));
        Event event = new Event();
        event.setUser(owner);
        applyForm(event, form);
        event.setStatus(EventStatus.DRAFT);
        return eventRepository.save(event);
    }

    @Override
    public Event update(Long id, EventForm form) {
        Event event = findWithDetails(id);
        if (event.getBooking() != null) {
            throw new IllegalStateException("Event details are locked after a booking request is created.");
        }
        applyForm(event, form);
        return eventRepository.save(event);
    }

    @Override
    public void delete(Long id) {
        Event event = findWithDetails(id);
        if (event.getBooking() != null) {
            throw new IllegalStateException("This event has a booking record and cannot be removed.");
        }
        eventRepository.delete(event);
    }

    @Override
    public void addDecoration(Long eventId, Long itemId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Choose a quantity of at least one.");
        }

        Event event = findWithDetails(eventId);
        ensureEditable(event);
        DecorationItem item = decorationItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("That decoration item could not be found."));
        if (!item.isActive()) {
            throw new IllegalArgumentException("That decoration item is not currently available.");
        }

        EventDecoration existing = event.getDecorations().stream()
                .filter(line -> line.getItem().getId().equals(itemId))
                .findFirst()
                .orElse(null);
        int newQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
        if (newQuantity > item.getAvailableQuantity()) {
            throw new IllegalArgumentException("Only " + item.getAvailableQuantity() + " of that item are available.");
        }

        if (existing == null) {
            event.addDecoration(new EventDecoration(item, quantity, item.getPrice()));
        } else {
            existing.setQuantity(newQuantity);
            existing.setSubtotal(existing.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));
        }
        eventRepository.save(event);
    }

    @Override
    public void removeDecoration(Long eventId, Long decorationId) {
        Event event = findWithDetails(eventId);
        ensureEditable(event);
        EventDecoration decoration = eventDecorationRepository.findByIdAndEventId(decorationId, eventId)
                .orElseThrow(() -> new ResourceNotFoundException("That selected decoration could not be found."));
        event.removeDecoration(decoration);
        eventRepository.save(event);
    }

    private void applyForm(Event event, EventForm form) {
        event.setEventName(form.getEventName().trim());
        event.setEventType(form.getEventType().trim());
        event.setEventDate(form.getEventDate());
        event.setVenue(form.getVenue().trim());
        event.setGuestCount(form.getGuestCount());
        event.setBudget(form.getBudget());
        if (form.getThemeId() == null) {
            event.setTheme(null);
        } else {
            Theme theme = themeRepository.findById(form.getThemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("That theme could not be found."));
            if (!theme.isActive()) {
                throw new IllegalArgumentException("Choose an active theme.");
            }
            event.setTheme(theme);
        }
    }

    private void ensureEditable(Event event) {
        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalStateException("Decoration selections are locked after the booking request is sent.");
        }
    }
}
