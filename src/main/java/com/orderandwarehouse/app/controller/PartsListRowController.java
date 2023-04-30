package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.service.PartsListRowService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products/partslist")
@RequiredArgsConstructor
@Validated
public class PartsListRowController {
    private final PartsListRowService service;

    @GetMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> getPartsListByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(service.getPartsListByProductId(productId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PartsListRow> add(@RequestBody @Valid PartsListRowDto dto) {
        return new ResponseEntity<>(service.add(dto), HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> addAllToProduct(@PathVariable @NotNull @Min(value = 1) Long productId, @RequestBody @Valid List<PartsListRowDto> partsList) {
        return new ResponseEntity<>(service.addAllToProduct(productId, partsList), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> updateAllBelongingToProduct(@PathVariable @NotNull @Min(value = 1) Long productId, @RequestBody @Valid Set<PartsListRowDto> partsList) {
        return new ResponseEntity<>(service.updateAllBelongingToProduct(productId, partsList), HttpStatus.OK);
    }

    @DeleteMapping("/delete/single/row/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull @Min(value = 1) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpStatus> deleteAllByProductId(@PathVariable Long productId) {
        service.deleteAllByProductId(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
