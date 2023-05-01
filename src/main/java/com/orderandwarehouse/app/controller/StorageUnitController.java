package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.service.StorageUnitService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class StorageUnitController {
    private final StorageUnitService service;

    @GetMapping
    public ResponseEntity<List<StorageUnit>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StorageUnit> getById(@PathVariable @NotNull @Min(value = 1) Long id) {
        return service.getById(id)
                .map(su -> new ResponseEntity<>(su, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<StorageUnit> add(@RequestBody StorageUnitDto storageUnitDto) {
        return new ResponseEntity<>(service.add(storageUnitDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StorageUnit> update(@PathVariable @NotNull @Min(value = 1) Long id, @RequestBody StorageUnitDto storageUnitDto) {
        if(!id.equals(storageUnitDto.getId())) throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(storageUnitDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull @Min(value = 1) Long id) {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/component/{component_id}")
    public ResponseEntity<List<StorageUnit>> getAllByComponentId(@PathVariable(name = "component_id") @NotNull @Min(value = 1) Long componentId) {
        return new ResponseEntity<>(service.getAllByComponentId(componentId), HttpStatus.OK);
    }
}
