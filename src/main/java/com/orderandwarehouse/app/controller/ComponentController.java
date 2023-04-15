package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.service.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComponentController {
    private final ComponentService service;

    @GetMapping
    public String testGetMapping(){
        return "Components will be listed here!";
    }
}
