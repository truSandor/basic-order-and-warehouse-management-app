package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.converter.OrderConverter;
import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService service;
    private final OrderConverter converter;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Order>> getByProductNameLike(@RequestParam String nameLike) {
        return new ResponseEntity<>(service.getByProductNameLike(nameLike), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Order> add(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(service.add(converter.dtoToEntity(orderDto)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(service.update(id, converter.dtoToEntity(orderDto)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) {
         /*
        TODO check if ID exists? OK : NOT_FOUND
                if ID exists check if it's in use? StorageUnitStillInUseException : OK
        */
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

}
