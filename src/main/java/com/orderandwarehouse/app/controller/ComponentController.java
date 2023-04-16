package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.service.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComponentController {
    private final ComponentService service;

    @GetMapping
    public ResponseEntity<List<Component>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
}
