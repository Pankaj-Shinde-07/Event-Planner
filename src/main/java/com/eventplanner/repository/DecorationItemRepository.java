package com.eventplanner.repository;

import com.eventplanner.entity.DecorationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DecorationItemRepository extends JpaRepository<DecorationItem, Long> {
    List<DecorationItem> findAllByActiveTrueOrderByCategoryAscItemNameAsc();
}
