package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.StorageUnit;
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

    public ComponentDto entityToDto(Component entity) {
        return ComponentDto.builder()
                .name(entity.getName())
                .type(entity.getType())
                .primaryValue(entity.getPrimaryValue())
                .primaryUnit(entity.getPrimaryUnit())
                .secondaryValue(entity.getSecondaryValue())
                .secondaryUnit(entity.getSecondaryUnit())
                .tolerance(entity.getTolerance())
                .packageDimensions(entity.getPackageDimensions())
                .weightInGrammes(entity.getWeightInGrammes())
                .manufacturerId(entity.getManufacturerId())
                .traderComponentId(entity.getTraderComponentId())
                .storageUnitIds(entity.getStorageUnits().stream().map(StorageUnit::getId).toList())
                .partsListRowsIds(entity.getPartsListRows().stream().map(PartsListRow::getId).toList())
                .build();
    }
}
