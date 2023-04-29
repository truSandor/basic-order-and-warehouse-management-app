package com.orderandwarehouse.app.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class PartsListRowDto {
    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    private Long id;
    @NotNull
    private Long productId;
    @NotNull
    private Long componentId;
    @NotNull
    @DecimalMin("0.0")
    private Double quantity;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String unit = "pcs";
    @Size(max = 5000, message = MAX_SIZE_MESSAGE)
    private String Comment;
    @Null
    private LocalDateTime dateAdded;
    @Null
    private LocalDateTime dateModified;
}
