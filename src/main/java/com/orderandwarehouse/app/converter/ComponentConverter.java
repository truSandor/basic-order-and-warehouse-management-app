package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;

@org.springframework.stereotype.Component
public class ComponentConverter {
    public Component dtoToEntity(ComponentDto dto) {
        Component entity = new Component();
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
        return entity;
    }
}
