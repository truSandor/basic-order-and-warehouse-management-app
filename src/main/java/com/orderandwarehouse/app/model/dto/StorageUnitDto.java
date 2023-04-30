package com.orderandwarehouse.app.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class StorageUnitDto {
    @Min(value = 1)
    private Long id;
    @Min(value = 1)
    private Long componentId;
    @DecimalMin(value = "0.0")
    private Double quantity;
    @NotNull
    @Min(value = 1)
    private Integer row;
    @NotNull
    @Min(value = 1)
    private Integer column;
    @NotNull
    @Min(value = 1)
    private Integer shelf;
    private boolean full;
}
