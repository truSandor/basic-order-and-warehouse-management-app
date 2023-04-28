package com.orderandwarehouse.app.exception;

import lombok.Getter;

@Getter
public class OrderInProgressException extends IllegalStateException {
    private Long orderId;

    public OrderInProgressException(Long orderId) {
        super(String.format("Order '%d' is in progress!", orderId));
        this.orderId = orderId;
    }
}
