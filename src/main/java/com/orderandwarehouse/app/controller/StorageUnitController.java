package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.service.StorageUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StorageUnitController {
    private final StorageUnitService service;

    @GetMapping
    public ResponseEntity<List<StorageUnit>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
}
