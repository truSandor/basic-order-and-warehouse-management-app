package com.orderandwarehouse.app.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.orderandwarehouse.app.util.Constants.*;

@Getter
@Setter
@Builder
public class ProductDto {

    @Min(value = 1, message = MIN_MESSAGE)
    private Long id;
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    @NotBlank(message = NAME_NOT_BLANK_MESSAGE)
    private String name;
    @Size(max = 10, message = MAX_SIZE_MESSAGE)
    private String version;
    @Size(max = 8, message = MAX_SIZE_MESSAGE)
    @Pattern(regexp = DIMENSIONS_REGEX, message = DIMENSIONS_MESSAGE)
    private String dimensions;
    @Min(value = 0, message = MIN_MESSAGE)
    private Integer weightInGrammes;
}
