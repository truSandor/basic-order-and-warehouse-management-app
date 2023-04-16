package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.repository.StorageUnitDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageUnitService {
    private final StorageUnitDao storageUnitDao;

    public List<StorageUnit> getAll() {
        return storageUnitDao.findAllByVisibleTrueOrderByIdAsc();
    }
}
