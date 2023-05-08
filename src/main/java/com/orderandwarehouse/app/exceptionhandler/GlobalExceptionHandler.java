package com.orderandwarehouse.app.exceptionhandler;

import com.orderandwarehouse.app.exception.*;
import jakarta.validation.ConstraintViolationException;
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
import java.util.InputMismatchException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> body = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(cv -> body.put(cv.getInvalidValue().toString(), cv.getMessage()));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<String> HandleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage() != null ? ex.getMessage() : "Record not found!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InputMismatchException.class)
    protected ResponseEntity<String> HandleOtherExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
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
        body.put("orderIds", ex.getActiveOrderIds().toString());
        body.put("hasPartsList", String.valueOf(ex.isHasPartsList()));
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }
}
