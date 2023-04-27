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
        return new ResponseEntity<>(service.update(id, storageUnitDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) throws SQLException {
        service.delete(id);
        //todo check what happens if i try to delete one that is in use -->deletes it, needs fixing!
        //todo create exception handler, check if this returns NOT_FOUND or OK if exception happens
        return HttpStatus.OK;

    }

    @GetMapping("/component/{component_id}")
    public ResponseEntity<List<StorageUnit>> getAllByComponentId(@PathVariable(name = "component_id") Long componentId) {
        return new ResponseEntity<>(service.getAllByComponentId(componentId), HttpStatus.OK);
    }
}
