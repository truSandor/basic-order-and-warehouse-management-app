package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.converter.StorageUnitConverter;
import com.orderandwarehouse.app.exception.StorageUnitStillInUseException;
import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.repository.StorageUnitDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageUnitService {
    private final StorageUnitDao storageUnitDao;
    private final StorageUnitConverter converter;

    public List<StorageUnit> getAll() {
        return storageUnitDao.findAllByIdNotNullOrderByRowAscColumnAscShelfAsc();
    }

    public Optional<StorageUnit> getById(Long id) {
        return storageUnitDao.findById(id);
    }

    public StorageUnit add(StorageUnitDto dto) {
        return storageUnitDao.save(converter.dtoToEntityForAdding(dto));
    }

    public StorageUnit update(StorageUnitDto dto) {
        return storageUnitDao.save(converter.dtoToEntityForUpdating(dto));
    }

    public void delete(Long id) {
        StorageUnit storageUnit = storageUnitDao.findById(id).orElseThrow(NoSuchElementException::new);
        if (!storageUnit.isEmpty())
            throw new StorageUnitStillInUseException(id, storageUnit.getComponent().getId(), storageUnit.getQuantity());
        storageUnitDao.deleteById(id);
    }

    public List<StorageUnit> getAllByComponentId(Long componentId) {
        return storageUnitDao.findAllByComponent_Id(componentId);
    }
}
