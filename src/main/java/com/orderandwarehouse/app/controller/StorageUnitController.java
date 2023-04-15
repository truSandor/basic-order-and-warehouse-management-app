package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.service.StorageUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StorageUnitController {
    private final StorageUnitService service;

    @GetMapping
    public String testGetMapping(){
        return "Storage units will be listed here!";
    }
}
