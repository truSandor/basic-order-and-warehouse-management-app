package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.repository.ComponentDao;
import com.orderandwarehouse.app.repository.StorageUnitDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageUnitService {
    private final StorageUnitDao storageUnitDao;
    private final ComponentDao componentDao;

    public List<StorageUnit> getAll() {
        return storageUnitDao.findAllByIdNotNullOrderByRowAscColumnAscShelfAsc();
    }

    public Optional<StorageUnit> getById(Long id) {
        return storageUnitDao.findById(id);
    }

    public StorageUnit add(@Valid StorageUnit storageUnit) {
        return storageUnitDao.save(storageUnit);
    }

    //intended: doesn't update row/column/shelf
    public StorageUnit update(Long id, StorageUnitDto dto) {
        StorageUnit storageUnit = storageUnitDao.findById(id).orElseThrow(NoSuchElementException::new);
        Component component = componentDao.findById(dto.getComponentId())
                .orElseThrow(
                        () -> new NoSuchElementException(
                                String.format("Component '%d' not found!", dto.getComponentId())
                        ));
        storageUnit.setComponent(component);
        storageUnit.setQuantity(dto.getQuantity());
        storageUnit.setFull(dto.isFull());
        return storageUnitDao.save(storageUnit);
    }

    public void delete(Long id) {
        storageUnitDao.deleteById(id);
        //check commit 9ea0e309 if you want to reroll
    }

    public List<StorageUnit> getAllByComponentId(Long componentId) {
        return storageUnitDao.findAllByComponent_Id(componentId);
    }
}
