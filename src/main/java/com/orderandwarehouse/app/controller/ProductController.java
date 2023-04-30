package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;
import com.orderandwarehouse.app.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable @NotNull @Min(value = 1) Long id) {
        return service.getById(id)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Product>> getByNameLike(@RequestParam @Size(max = 40) String nameLike) {
        return new ResponseEntity<>(service.getByNameLike(nameLike), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> add(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(service.add(productDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> update(@PathVariable @NotNull @Min(value = 1) Long id, @RequestBody @Valid ProductDto productDto) {
        if(!id.equals(productDto.getId())) throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(productDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull @Min(value = 1) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
