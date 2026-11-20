package com.eventplanner.repository;

import com.eventplanner.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAllByActiveTrueOrderByThemeNameAsc();
}
