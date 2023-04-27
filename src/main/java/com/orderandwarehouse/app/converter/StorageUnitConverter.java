package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.repository.ComponentDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class StorageUnitConverter {
    private final ComponentDao componentDao;

    public StorageUnit dtoToEntity(StorageUnitDto dto) {
        StorageUnit entity = new StorageUnit();
        if (dto.getComponentId() != null) {
            entity.setComponent(
                    componentDao.findById(dto.getComponentId())
                            .orElseThrow(() ->
                                    new NoSuchElementException(
                                            String.format("Component '%d' not found!", dto.getComponentId()
                                            )
                                    )
                            )
            );
        }
        entity.setRow(dto.getRow());
        entity.setColumn(dto.getColumn());
        entity.setShelf(dto.getShelf());
        entity.setQuantity(dto.getQuantity());
        entity.setFull(dto.isFull());
        return entity;
    }
}
