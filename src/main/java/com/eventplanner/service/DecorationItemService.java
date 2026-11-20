package com.eventplanner.service;

import com.eventplanner.entity.DecorationItem;

import java.util.List;

public interface DecorationItemService {
    List<DecorationItem> findActiveItems();
}
