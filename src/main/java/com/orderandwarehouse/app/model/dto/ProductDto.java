package com.orderandwarehouse.app.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class ProductDto {
    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Min(value = 1)
    private Long id;
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    @NotBlank(message = "Name must not be blank!")
    private String name;
    @Size(max = 10, message = MAX_SIZE_MESSAGE)
    private String version;
    @Size(max = 8)
    @Pattern(regexp = "^\\d{1,2}x\\d{1,2}x\\d{1,2}$", message = "Dimensions pattern: \"LLxWWxHH\" in cm")
    private String dimensions;
    @Min(value = 0)
    private Integer weightInGrammes;
}
