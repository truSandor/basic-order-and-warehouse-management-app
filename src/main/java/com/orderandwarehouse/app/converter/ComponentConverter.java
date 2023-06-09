package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.repository.ComponentDao;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ComponentConverter {
    private final ComponentDao componentDao;

    public Component dtoToEntityForAdding(ComponentDto dto) {
        Component entity = new Component();
        setAttributes(dto, entity);
        entity.setDateAdded(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        return entity;
    }

    public Component dtoToEntityForUpdating(ComponentDto dto) {
        Component entity = componentDao.findById(dto.getId()).orElseThrow();
        setAttributes(dto, entity);
        return entity;
    }

    private void setAttributes(ComponentDto dto, Component entity) {
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
        entity.setDateModified(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }
}
