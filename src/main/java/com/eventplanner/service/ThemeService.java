package com.eventplanner.service;

import com.eventplanner.entity.Theme;

import java.util.List;

public interface ThemeService {
    List<Theme> findActiveThemes();
}
