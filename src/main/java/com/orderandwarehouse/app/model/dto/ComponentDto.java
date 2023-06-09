package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Type;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.orderandwarehouse.app.util.Constants.*;

@Getter
@Setter
@Builder
public class ComponentDto {

    @Min(value = 1, message = MIN_MESSAGE)
    private Long Id;
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    @NotBlank(message = "Name must not be blank!")
    private String name;
    @NotNull
    private Type type;
    @DecimalMin(value = "0.0", message = MIN_MESSAGE)
    private Double primaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String primaryUnit;
    @DecimalMin(value = "0.0", message = MIN_MESSAGE)
    private Double secondaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String secondaryUnit;
    @Min(value = 0, message = MIN_MESSAGE)
    private Integer tolerance;
    @Size(max = 8, message = MAX_SIZE_MESSAGE)
    @Pattern(regexp = DIMENSIONS_REGEX, message = DIMENSIONS_MESSAGE)
    private String packageDimensions;
    @DecimalMin(value = "0.0", message = MIN_MESSAGE)
    private Double weightInGrammes;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String manufacturerId;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String traderComponentId;
}
