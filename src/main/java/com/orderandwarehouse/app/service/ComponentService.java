package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.repository.ComponentDao;
import com.orderandwarehouse.app.repository.StorageUnitDao;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentDao componentDao;
    private final StorageUnitDao storageUnitDao;

    public List<Component> getAll() {
        return componentDao.findAllByVisibleTrueOrderByIdAsc();
    }
}
