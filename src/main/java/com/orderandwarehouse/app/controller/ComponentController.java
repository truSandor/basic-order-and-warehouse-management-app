package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.converter.ComponentConverter;
import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.service.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComponentController {
    private final ComponentService service;
    private final ComponentConverter converter;

    @GetMapping
    public ResponseEntity<List<Component>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Component> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Component> add(@RequestBody ComponentDto componentDto) {
        return new ResponseEntity<>(service.add(converter.dtoToEntity(componentDto)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Component> update(@PathVariable Long id, @RequestBody ComponentDto componentDto) {
        return new ResponseEntity<>(service.update(id, converter.dtoToEntity(componentDto)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        service.delete(id);
        //todo check what happens if i try to delete one that is in use
        //todo create exception handler, check if this returns NOT_FOUND or OK if exception happens
        //todo return not_found if doesn't exist
        return HttpStatus.OK;
    }

    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Component>> getByNameLike(@RequestParam String nameLike) {
        return new ResponseEntity<>(service.getByNameLike(nameLike), HttpStatus.OK);
    }
}
