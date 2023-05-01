package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Type;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
public class ComponentDto {

    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Min(value = 1)
    private Long Id;
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    @NotBlank(message = "Name must not be blank!")
    private String name;
    @NotNull
    private Type type;
    @DecimalMin(value = "0.0")
    private Double primaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String primaryUnit;
    @DecimalMin(value = "0.0")
    private Double secondaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String secondaryUnit;
    @Min(value = 0L)
    private Integer tolerance;
    @Size(max = 8)
    @Pattern(regexp = "^\\d{1,2}x\\d{1,2}x\\d{1,2}$", message = "Dimensions pattern: \"LLxWWxHH\" in cm")
    private String packageDimensions;
    @DecimalMin(value = "0.0")
    private Double weightInGrammes;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String manufacturerId;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String traderComponentId;
}
