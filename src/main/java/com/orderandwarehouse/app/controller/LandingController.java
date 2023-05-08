package com.orderandwarehouse.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class LandingController {
    @GetMapping
    public String welcome() {
        return "<h1>Welcome!</h1>" +
                "<p>try these endpoints:</p>" +
                "<p><a href=\"/orders\">orders</a></p>" +
                "<p><a href=\"/products\">products</a></p>" +
                "<p><a href=\"/products/partslist/1\">parts list of product #1</a></p>" +
                "<p><a href=\"/components\">components</a></p>" +
                "<p><a href=\"/storage\">storage units</a></p>";
    }
}
