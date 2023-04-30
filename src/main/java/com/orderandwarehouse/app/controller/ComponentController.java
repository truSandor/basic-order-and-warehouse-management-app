package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.service.ComponentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class ComponentController {
    private final ComponentService service;

    @GetMapping
    public ResponseEntity<List<Component>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Component> getById(@PathVariable @Min(value = 1) Long id) {
        return service.getById(id)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Component> add(@RequestBody @Valid ComponentDto componentDto) {
        return new ResponseEntity<>(service.add(componentDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Component> update(@PathVariable @NotNull @Min(value = 1) Long id, @RequestBody @Valid ComponentDto componentDto) {
        if(!id.equals(componentDto.getId())) throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(componentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(value = 1) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Component>> getByNameLike(@RequestParam @Size(max = 40) String nameLike) {
        return new ResponseEntity<>(service.getByNameLike(nameLike), HttpStatus.OK);
    }
}
