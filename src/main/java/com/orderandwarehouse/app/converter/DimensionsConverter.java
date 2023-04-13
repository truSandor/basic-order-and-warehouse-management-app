package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Dimensions;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DimensionsConverter implements AttributeConverter<Dimensions, String> {

    @Override
    public String convertToDatabaseColumn(Dimensions dimensions) {
        return dimensions.toString();
    }

    @Override
    public Dimensions convertToEntityAttribute(String dimensionsAsString) {
        return new Dimensions(dimensionsAsString);
    }
}
