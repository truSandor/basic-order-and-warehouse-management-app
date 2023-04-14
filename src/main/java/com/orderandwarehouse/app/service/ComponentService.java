package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.repository.ComponentDao;
import com.orderandwarehouse.app.repository.StorageUnitDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentDao componentDao;
    private final StorageUnitDao storageUnitDao;
}
