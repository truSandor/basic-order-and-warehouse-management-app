package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.repository.StorageUnitDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageUnitService {
    private final StorageUnitDao storageUnitDao;
}
