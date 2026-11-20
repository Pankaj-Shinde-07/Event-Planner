package com.eventplanner.config;

import com.eventplanner.entity.DecorationItem;
import com.eventplanner.entity.Theme;
import com.eventplanner.entity.User;
import com.eventplanner.entity.enums.UserRole;
import com.eventplanner.repository.DecorationItemRepository;
import com.eventplanner.repository.ThemeRepository;
import com.eventplanner.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final DecorationItemRepository decorationItemRepository;

    public DataInitializer(UserRepository userRepository, ThemeRepository themeRepository,
                           DecorationItemRepository decorationItemRepository) {
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.decorationItemRepository = decorationItemRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("Demo Customer", "demo@example.com", "", UserRole.CUSTOMER));
        }
        if (themeRepository.count() == 0) {
            themeRepository.saveAll(List.of(
                    new Theme("Birthday Spark", "Bright balloons and cheerful colours for a birthday.", "", new BigDecimal("2500.00")),
                    new Theme("Elegant Anniversary", "Soft florals and warm details for an anniversary.", "", new BigDecimal("3800.00")),
                    new Theme("Pastel Welcome", "A gentle pastel setup for a baby shower or naming ceremony.", "", new BigDecimal("3200.00"))
            ));
        }
        if (decorationItemRepository.count() == 0) {
            decorationItemRepository.saveAll(List.of(
                    new DecorationItem("Balloon arch", "Backdrop", "A colourful balloon arch for the main photo area.", new BigDecimal("1800.00"), 8, ""),
                    new DecorationItem("LED number lights", "Lighting", "Warm LED number lights for a milestone celebration.", new BigDecimal("900.00"), 12, ""),
                    new DecorationItem("Fresh flower bunches", "Flowers", "Seasonal flowers arranged for tables or a welcome stand.", new BigDecimal("650.00"), 24, ""),
                    new DecorationItem("Welcome board", "Signage", "A personalized welcome board for the entrance.", new BigDecimal("1200.00"), 6, ""),
                    new DecorationItem("Fairy light strings", "Lighting", "Warm string lights for an indoor or outdoor setup.", new BigDecimal("500.00"), 15, "")
            ));
        }
    }
}
