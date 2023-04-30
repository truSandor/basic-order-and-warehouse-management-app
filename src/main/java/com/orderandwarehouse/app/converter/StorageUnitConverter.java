package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.exception.NoIdException;
import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.repository.ComponentDao;

import com.orderandwarehouse.app.repository.StorageUnitDao;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class StorageUnitConverter {
    private final StorageUnitDao storageUnitDao;
    private final ComponentDao componentDao;

    public StorageUnit dtoToEntityForAdding(StorageUnitDto dto) {
        StorageUnit entity = new StorageUnit();
        entity.setRow(dto.getRow());
        entity.setColumn(dto.getColumn());
        entity.setShelf(dto.getShelf());
        setAttributes(dto, entity);
        entity.setDateAdded(LocalDateTime.now());
        return entity;
    }

    public StorageUnit dtoToEntityForUpdating(StorageUnitDto dto) {
        if (dto.getId() == null) throw new NoIdException(dto.getClass());
        StorageUnit entity = storageUnitDao.findById(dto.getId()).orElseThrow();
        if (!(entity.getRow().equals(dto.getRow()) &&
                entity.getColumn().equals(dto.getColumn()) &&
                entity.getShelf().equals(dto.getShelf())))
            throw new InputMismatchException(
                    "Given storage unit's row-column-shelf doesn't match with record!" +
                            " Check if you used the correct Id!");
        setAttributes(dto, entity);
        return entity;
    }

    private void setAttributes(StorageUnitDto dto, StorageUnit entity) {
        entity.setComponent(null);
        if (dto.getComponentId() != null) {
            entity.setComponent(componentDao.findById(dto.getComponentId())
                    .orElseThrow(() -> new NoSuchElementException(
                            String.format(
                                    "Component '%d' not found!",
                                    dto.getComponentId()))));
        }
        entity.setQuantity(dto.getQuantity());
        entity.setFull(dto.isFull());
        entity.setDateModified(LocalDateTime.now());
    }
}
