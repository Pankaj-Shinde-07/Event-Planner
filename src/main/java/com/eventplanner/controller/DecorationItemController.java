package com.eventplanner.controller;

import com.eventplanner.service.DecorationItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/decorations")
public class DecorationItemController {

    private final DecorationItemService decorationItemService;

    public DecorationItemController(DecorationItemService decorationItemService) {
        this.decorationItemService = decorationItemService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", decorationItemService.findActiveItems());
        return "decorations/list";
    }
}
