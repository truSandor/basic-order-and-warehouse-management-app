package com.orderandwarehouse.app.exceptionhandler;

import com.orderandwarehouse.app.exception.ComponentStillInUseException;
import com.orderandwarehouse.app.exception.OrderInProgressException;
import com.orderandwarehouse.app.exception.ProductStillInUseException;
import com.orderandwarehouse.app.exception.StorageUnitStillInUseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> body = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fe -> body.put(fe.getField(), fe.getDefaultMessage()));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> HandleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> body = new HashMap<>();
        String message = ex.getMessage() != null ? ex.getMessage() : "Record not found!";
        body.put("message", message);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ComponentStillInUseException.class)
    protected ResponseEntity<Object> handleComponentStillInUseException(ComponentStillInUseException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("componentId", ex.getComponentId().toString());
        body.put("storageUnitIds", ex.getStorageUnitIds().toString());
        body.put("productIds", ex.getProductIds().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(StorageUnitStillInUseException.class)
    protected ResponseEntity<Object> handleStorageUnitStillInUseException(StorageUnitStillInUseException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("storageUnitId", ex.getStorageUnitId().toString());
        body.put("productIds", ex.getComponentId().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(OrderInProgressException.class)
    protected ResponseEntity<Object> HandleOrderInProgressException(OrderInProgressException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("orderId", ex.getOrderId().toString());
        body.put("status", ex.getStatus().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ProductStillInUseException.class)
    protected ResponseEntity<Object> HandleProductStillInUseException(ProductStillInUseException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("productId", ex.getProductId().toString());
        body.put("orderIds", ex.getOrderIds().toString());
        body.put("hasPartsList", String.valueOf(ex.isHasPartsList()));
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }
}
