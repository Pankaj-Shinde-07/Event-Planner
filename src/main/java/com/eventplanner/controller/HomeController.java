package com.eventplanner.controller;

import com.eventplanner.entity.enums.BookingStatus;
import com.eventplanner.service.BookingService;
import com.eventplanner.service.DecorationItemService;
import com.eventplanner.service.EventService;
import com.eventplanner.service.ThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final EventService eventService;
    private final BookingService bookingService;
    private final ThemeService themeService;
    private final DecorationItemService decorationItemService;

    public HomeController(EventService eventService, BookingService bookingService,
                          ThemeService themeService, DecorationItemService decorationItemService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
        this.themeService = themeService;
        this.decorationItemService = decorationItemService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        var events = eventService.findAll();
        var bookings = bookingService.findAll();
        var upcomingEvents = events.stream()
                .filter(event -> !event.getEventDate().isBefore(LocalDate.now()))
                .limit(4)
                .toList();

        model.addAttribute("eventCount", events.size());
        model.addAttribute("bookingCount", bookings.size());
        model.addAttribute("pendingBookingCount", bookings.stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.PENDING).count());
        model.addAttribute("themeCount", themeService.findActiveThemes().size());
        model.addAttribute("itemCount", decorationItemService.findActiveItems().size());
        model.addAttribute("upcomingEvents", upcomingEvents);
        return "index";
    }
}
