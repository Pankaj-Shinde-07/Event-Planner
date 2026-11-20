package com.eventplanner.service.impl;

import com.eventplanner.entity.DecorationItem;
import com.eventplanner.repository.DecorationItemRepository;
import com.eventplanner.service.DecorationItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DecorationItemServiceImpl implements DecorationItemService {

    private final DecorationItemRepository decorationItemRepository;

    public DecorationItemServiceImpl(DecorationItemRepository decorationItemRepository) {
        this.decorationItemRepository = decorationItemRepository;
    }

    @Override
    public List<DecorationItem> findActiveItems() {
        return decorationItemRepository.findAllByActiveTrueOrderByCategoryAscItemNameAsc();
    }
}
