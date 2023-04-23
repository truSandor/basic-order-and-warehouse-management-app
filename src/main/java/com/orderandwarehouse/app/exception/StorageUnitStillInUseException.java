package com.orderandwarehouse.app.exception;

public class StorageUnitStillInUseException extends IllegalStateException {
    public StorageUnitStillInUseException(String s) {
        super(s);
    }
}
