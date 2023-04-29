package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.service.PartsListRowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products/partslist")
@RequiredArgsConstructor
public class PartsListRowController {
    private final PartsListRowService service;

    @GetMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> getPartsListByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(service.getPartsListByProductId(productId), HttpStatus.OK);
    }

    @PostMapping
    //todo needs dto to reference product and components with ids
    public ResponseEntity<PartsListRow> add(@RequestBody @Valid PartsListRow partsListRow) { //might need a dto or set Components to be represented by Id
        return new ResponseEntity<>(service.add(partsListRow), HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    //todo needs dto to reference product and components with ids
    public ResponseEntity<List<PartsListRow>> addAllToProduct(@PathVariable Long productId, @RequestBody @Valid /*can a list be validated? */ List<PartsListRow> partsList) {
        return new ResponseEntity<>(service.addAllToProduct(productId, partsList), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    //todo needs dto to reference product and components with ids
    public ResponseEntity<List<PartsListRow>> updateAllBelongingToProduct(@PathVariable Long productId, @RequestBody @Valid /*same as above*/ Set<PartsListRow> partsList) {
        return new ResponseEntity<>(service.updateAllBelongingToProduct(productId, partsList), HttpStatus.OK);
    }

    @DeleteMapping("/delete/single/row/{id}")
    public HttpStatus delete(@PathVariable Long id) {
         /*
        TODO check if ID exists? OK : NOT_FOUND
                if ID exists check if it's in use? StorageUnitStillInUseException : OK
        */
        service.delete(id);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{productId}")
    public HttpStatus deleteAllByProductId(@PathVariable Long productId) {
          /*
        TODO check if ID exists? OK : NOT_FOUND
                if ID exists check if it's in use? StorageUnitStillInUseException : OK
        */
        service.deleteAllByProductId(productId);
        return HttpStatus.OK;
    }
}
