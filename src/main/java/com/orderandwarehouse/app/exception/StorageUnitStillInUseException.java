package com.orderandwarehouse.app.exception;

import lombok.Getter;

@Getter
public class StorageUnitStillInUseException extends IllegalStateException {
    private Long storageUnitId;
    private Long componentId;
    private Double quantity;

    public StorageUnitStillInUseException(Long storageUnitId, Long componentId, Double quantity) {
        super(String.format("Storage Unit '%d' is still in use", storageUnitId));
        this.storageUnitId = storageUnitId;
        this.componentId = componentId;
        this.quantity = quantity;
    }
}
