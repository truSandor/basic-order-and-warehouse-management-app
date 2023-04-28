package com.orderandwarehouse.app.exception;

import lombok.Getter;

import java.util.List;
@Getter
public class ProductStillInUseException extends IllegalStateException {
    private Long productId;
    private List<Long> orderIds;
    private boolean hasPartsList;


    public ProductStillInUseException(Long productId, List<Long> orderIds, boolean hasPartsList) {
        super(String.format("Product '%d' has active orders or parts list. Close the orders and delete parts list to proceed!", productId));
        this.productId = productId;
        this.orderIds = orderIds;
        this.hasPartsList = hasPartsList;
    }
}
