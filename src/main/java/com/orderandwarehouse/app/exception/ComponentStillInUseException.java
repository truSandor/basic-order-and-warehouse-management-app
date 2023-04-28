package com.orderandwarehouse.app.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ComponentStillInUseException extends IllegalStateException {
    private Long componentId;
    private List<Long> storageUnitIds;
    private List<Long> productIds;

    public ComponentStillInUseException(Long componentId, List<Long> storageUnitIds, List<Long> productIds) {
        super(String.format("Component '%d' is still in use!", componentId));
        this.componentId = componentId;
        this.storageUnitIds = storageUnitIds;
        this.productIds = productIds;
    }
}
