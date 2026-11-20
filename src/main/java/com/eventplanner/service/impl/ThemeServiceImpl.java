package com.eventplanner.service.impl;

import com.eventplanner.entity.Theme;
import com.eventplanner.repository.ThemeRepository;
import com.eventplanner.service.ThemeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public List<Theme> findActiveThemes() {
        return themeRepository.findAllByActiveTrueOrderByThemeNameAsc();
    }
}
