package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.repository.ComponentDao;
import com.orderandwarehouse.app.repository.PartsListRowDao;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class PartsListRowConverter {
    private final PartsListRowDao partsListRowDao;
    private final ProductDao productDao;
    private final ComponentDao componentDao;

    public PartsListRow dtoToEntity(PartsListRowDto dto) {
        PartsListRow entity = partsListRowDao.findById(dto.getId())
                .orElse(new PartsListRow());
        //todo need to do a validation, if new partslist--> ok, if update then product can't be different
        entity.setProduct(productDao.findById(dto.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found!")));
        entity.setComponent(componentDao.findById(dto.getComponentId())
                .orElseThrow(() -> new NoSuchElementException("Component not found!")));
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());
        entity.setComment(dto.getComment());
        entity.setDateAdded(LocalDateTime.now()); //todo validation--> don't set it or check in service
        entity.setDateModified(LocalDateTime.now());
        return entity;
    }

    public PartsListRowDto entityToDto(PartsListRow entity) {
        PartsListRowDto dto = new PartsListRowDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setComponentId(entity.getComponent().getId());
        dto.setComponentName(entity.getComponent().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setComment(entity.getComment());
        dto.setDateAdded(entity.getDateAdded());
        dto.setDateModified(entity.getDateModified());
        return dto;
    }
}
