package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    private Long  productId;
    private Integer quantity;
    private LocalDateTime dateReceived;
    private LocalDateTime deadline;
    private LocalDateTime dateStarted;
    private LocalDateTime dateCompleted;
    private Status status;
}
