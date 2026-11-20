package com.eventplanner.dto;

import com.eventplanner.entity.Event;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EventForm {

    @NotBlank(message = "Enter an event name.")
    private String eventName;

    @NotBlank(message = "Choose an event type.")
    private String eventType;

    @NotNull(message = "Choose an event date.")
    @FutureOrPresent(message = "The event date must be today or later.")
    private LocalDate eventDate;

    @NotBlank(message = "Enter the venue or location.")
    private String venue;

    @Min(value = 1, message = "Guest count must be at least 1.")
    private int guestCount = 1;

    @NotNull(message = "Enter a budget.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Budget cannot be negative.")
    private BigDecimal budget = BigDecimal.ZERO;

    private Long themeId;

    public static EventForm from(Event event) {
        EventForm form = new EventForm();
        form.setEventName(event.getEventName());
        form.setEventType(event.getEventType());
        form.setEventDate(event.getEventDate());
        form.setVenue(event.getVenue());
        form.setGuestCount(event.getGuestCount());
        form.setBudget(event.getBudget());
        form.setThemeId(event.getTheme() == null ? null : event.getTheme().getId());
        return form;
    }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public Long getThemeId() { return themeId; }
    public void setThemeId(Long themeId) { this.themeId = themeId; }
}
