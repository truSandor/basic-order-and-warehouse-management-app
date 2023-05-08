package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ProductDao productDao;

    public Product dtoToEntityForAdding(ProductDto dto) {
        Product entity = new Product();
        setAttributes(dto, entity);
        entity.setDateAdded(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        return entity;
    }

    public Product dtoToEntityForUpdating(ProductDto dto) {
        Product entity = productDao.findById(dto.getId()).orElseThrow();
        setAttributes(dto, entity);
        return entity;
    }

    private void setAttributes(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setVersion(dto.getVersion());
        entity.setDimensions(dto.getDimensions());
        entity.setWeightInGrammes(dto.getWeightInGrammes());
        entity.setDateModified(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }
}
