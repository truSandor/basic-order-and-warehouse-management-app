package com.orderandwarehouse.app.model.dto;

import lombok.*;

@Getter
@Setter
public class ProductDto {
    private String name;
    private String version;
    private String dimensions;
    private Integer weightInGrammes;
}
