package com.orderandwarehouse.app.exception;

public class ComponentStillInUseException extends IllegalStateException{
    public ComponentStillInUseException(String s) {
        super(s);
    }
}
