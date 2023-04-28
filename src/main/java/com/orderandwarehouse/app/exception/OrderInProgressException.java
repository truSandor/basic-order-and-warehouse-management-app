package com.orderandwarehouse.app.exception;

import com.orderandwarehouse.app.model.Status;
import lombok.Getter;

@Getter
public class OrderInProgressException extends IllegalStateException {
    private Long orderId;
    private Status status;

    public OrderInProgressException(Long orderId, Status status) {
        super(String.format("Order '%d' is %s, and can't be deleted!", orderId, status.toString()));
        this.orderId = orderId;
        this.status = status;
    }
}
