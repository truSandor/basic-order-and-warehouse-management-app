package com.orderandwarehouse.app.exception;

public class NoIdException extends IllegalArgumentException{

    public <T> NoIdException(Class<T> c) {
        super(String.format("%s's Id can't be null!", c.getSimpleName()));
    }
}
