package com.orderandwarehouse.app.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
public class PartsListRowDto {
    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";
    private static final String MIN_MESSAGE = "Needs to be greater or equal to {value}!";

    @Min(value = 1, message = MIN_MESSAGE)
    private Long id;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Long productId;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Long componentId;
    @NotNull
    @DecimalMin(value = "0.0", message = MIN_MESSAGE)
    private Double quantity;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    @Builder.Default
    private String unit = "pcs";
    @Size(max = 5000, message = MAX_SIZE_MESSAGE)
    private String Comment;
}
