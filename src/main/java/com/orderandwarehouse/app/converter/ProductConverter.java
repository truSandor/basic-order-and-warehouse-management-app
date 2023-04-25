package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;

public class ProductConverter {

    public Product dtoToEntity(ProductDto dto) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setVersion(dto.getVersion());
        entity.setDimensions(dto.getDimensions());
        entity.setWeightInGrammes(dto.getWeightInGrammes());
        return entity;
    }
}
