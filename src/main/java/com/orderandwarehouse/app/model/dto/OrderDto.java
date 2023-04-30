package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {

    @Min(value = 1)
    private Long id;
    @NotNull
    @Min(value = 1)
    private Long productId;
    @NotNull
    @Min(value = 1)
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
