package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.orderandwarehouse.app.util.Constants.DATE_PATTERN;
import static com.orderandwarehouse.app.util.Constants.MIN_MESSAGE;

@Getter
@Setter
@Builder
public class OrderDto {

    @Min(value = 1, message = MIN_MESSAGE)
    private Long id;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Long productId;
    @NotNull
    @Min(value = 1, message = MIN_MESSAGE)
    private Integer quantity;
    @NotNull
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDateTime dateReceived;
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDateTime deadline;
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDateTime dateStarted;
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDateTime dateCompleted;
    @NotNull
    private Status status;
}
