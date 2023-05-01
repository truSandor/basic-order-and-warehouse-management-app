package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderDto {
    private static final String MIN_MESSAGE = "Needs to be greater or equal to {value}!";

    @Min(value = 1, message = MIN_MESSAGE)
    private Long id;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Long productId;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Integer quantity;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss.SS")
    private LocalDateTime dateReceived;
    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss.SS")
    private LocalDateTime deadline;
    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss.SS")
    private LocalDateTime dateStarted;
    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss.SS")
    private LocalDateTime dateCompleted;
    @NotNull
    private Status status;
}
