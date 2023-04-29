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

    public PartsListRow dtoToEntityForAdding(PartsListRowDto dto) {
        PartsListRow entity = new PartsListRow();
        entity.setProduct(productDao.findById(dto.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found!")));
        entity.setComponent(componentDao.findById(dto.getComponentId())
                .orElseThrow(() -> new NoSuchElementException("Component not found!")));
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());
        entity.setComment(dto.getComment());
        entity.setDateAdded(LocalDateTime.now());
        entity.setDateModified(LocalDateTime.now());
        return entity;
    }

    public PartsListRow dtoToEntityForUpdating(PartsListRowDto dto){
        PartsListRow entity = partsListRowDao.findById(dto.getId()).orElseThrow();
        entity.setComponent(componentDao.findById(dto.getComponentId())
                .orElseThrow(() -> new NoSuchElementException("Component not found!")));
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());
        entity.setComment(dto.getComment());
        entity.setDateModified(LocalDateTime.now());
        return entity;
    }
    
}
