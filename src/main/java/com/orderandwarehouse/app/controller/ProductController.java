package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
}
