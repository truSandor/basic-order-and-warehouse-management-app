package com.orderandwarehouse.app.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
public class StorageUnitDto {
    private static final String MIN_MESSAGE = "Needs to be greater or equal to {value}!";

    @Min(value = 1, message = MIN_MESSAGE)
    private Long id;
    @Min(value = 1, message = MIN_MESSAGE)
    private Long componentId;
    @DecimalMin(value = "0.0", message = MIN_MESSAGE)
    private Double quantity;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Integer row;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Integer column;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Integer shelf;
    private boolean full;
}
