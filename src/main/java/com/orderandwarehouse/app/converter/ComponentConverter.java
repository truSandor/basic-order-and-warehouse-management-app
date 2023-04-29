package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.repository.ComponentDao;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ComponentConverter {
    private final ComponentDao componentDao;

    public Component dtoToEntity(ComponentDto dto) {
        Component entity;
        if (dto.getId() == null) {
            entity = new Component();
            entity.setDateAdded(LocalDateTime.now());
        } else {
            entity = componentDao.findById(dto.getId()).orElseThrow();
        }
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setPrimaryValue(dto.getPrimaryValue());
        entity.setPrimaryUnit(dto.getPrimaryUnit());
        entity.setSecondaryValue(dto.getSecondaryValue());
        entity.setSecondaryUnit(dto.getSecondaryUnit());
        entity.setTolerance(dto.getTolerance());
        entity.setPackageDimensions(dto.getPackageDimensions());
        entity.setWeightInGrammes(dto.getWeightInGrammes());
        entity.setManufacturerId(dto.getManufacturerId());
        entity.setTraderComponentId(dto.getTraderComponentId());
        entity.setDateModified(LocalDateTime.now());
        return entity;
    }
}
