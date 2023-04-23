package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.converter.StorageUnitConverter;
import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.service.StorageUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StorageUnitController {
    private final StorageUnitService service;
    private final StorageUnitConverter converter;

    @GetMapping
    public ResponseEntity<List<StorageUnit>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StorageUnit> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(su -> new ResponseEntity<>(su, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<StorageUnit> add(@RequestBody StorageUnitDto storageUnitDto) {
        return new ResponseEntity<>(service.add(converter.dtoToEntity(storageUnitDto)), HttpStatus.OK);
    }

    //we can use it to change component and quantity too
    @PutMapping("/{id}")
    public ResponseEntity<StorageUnit> update(@PathVariable Long id, @RequestBody StorageUnitDto storageUnitDto) {
        return new ResponseEntity<>(service.update(id, converter.dtoToEntity(storageUnitDto)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus softDelete(@PathVariable Long id) throws SQLException {
        return service.softDelete(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    @GetMapping("/component/{component_id}")
    public ResponseEntity<List<StorageUnit>> getAllByComponentId(@PathVariable(name = "component_id") Long componentId) {
        return new ResponseEntity<>(service.getAllByComponentId(componentId), HttpStatus.OK);
    }
}
