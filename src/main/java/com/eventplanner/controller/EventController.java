package com.eventplanner.controller;

import com.eventplanner.dto.EventForm;
import com.eventplanner.service.BookingService;
import com.eventplanner.service.DecorationItemService;
import com.eventplanner.service.EventService;
import com.eventplanner.service.ThemeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final ThemeService themeService;
    private final DecorationItemService decorationItemService;
    private final BookingService bookingService;

    public EventController(EventService eventService, ThemeService themeService,
                           DecorationItemService decorationItemService, BookingService bookingService) {
        this.eventService = eventService;
        this.themeService = themeService;
        this.decorationItemService = decorationItemService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "events/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long themeId, Model model) {
        EventForm eventForm = new EventForm();
        eventForm.setThemeId(themeId);
        model.addAttribute("eventForm", eventForm);
        model.addAttribute("pageTitle", "Create an event");
        model.addAttribute("formAction", "/events");
        addThemes(model);
        return "events/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("eventForm") EventForm eventForm,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create an event");
            model.addAttribute("formAction", "/events");
            addThemes(model);
            return "events/form";
        }
        var event = eventService.create(eventForm);
        return "redirect:/events/" + event.getId();
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findWithDetails(id));
        model.addAttribute("items", decorationItemService.findActiveItems());
        return "events/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var event = eventService.findWithDetails(id);
        model.addAttribute("eventForm", EventForm.from(event));
        model.addAttribute("pageTitle", "Edit event");
        model.addAttribute("formAction", "/events/" + id + "/edit");
        model.addAttribute("event", event);
        addThemes(model);
        return "events/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("eventForm") EventForm eventForm,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit event");
            model.addAttribute("formAction", "/events/" + id + "/edit");
            model.addAttribute("event", eventService.findWithDetails(id));
            addThemes(model);
            return "events/form";
        }
        eventService.update(id, eventForm);
        return "redirect:/events/" + id;
    }

    @PostMapping("/{id}/decorations")
    public String addDecoration(@PathVariable Long id, @RequestParam Long itemId,
                                @RequestParam(defaultValue = "1") int quantity) {
        eventService.addDecoration(id, itemId, quantity);
        return "redirect:/events/" + id;
    }

    @PostMapping("/{id}/decorations/{decorationId}/remove")
    public String removeDecoration(@PathVariable Long id, @PathVariable Long decorationId) {
        eventService.removeDecoration(id, decorationId);
        return "redirect:/events/" + id;
    }

    @PostMapping("/{id}/booking")
    public String requestBooking(@PathVariable Long id) {
        bookingService.requestBooking(id);
        return "redirect:/bookings";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventService.delete(id);
        return "redirect:/events";
    }

    private void addThemes(Model model) {
        model.addAttribute("themes", themeService.findActiveThemes());
    }
}
