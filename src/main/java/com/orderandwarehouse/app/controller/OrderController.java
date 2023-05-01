package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.service.OrderService;
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

import static com.orderandwarehouse.app.util.Constants.MAX_SIZE_MESSAGE;
import static com.orderandwarehouse.app.util.Constants.MIN_MESSAGE;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class OrderController {
    private final OrderService service;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Order>> getByProductNameLike(@RequestParam @Size(max = 40, message = MAX_SIZE_MESSAGE) String nameLike) {
        return new ResponseEntity<>(service.getByProductNameLike(nameLike), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        return service.getById(id)
                .map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Order> add(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(service.add(orderDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id, @RequestBody @Valid OrderDto orderDto) {
        if (!id.equals(orderDto.getId())) throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(orderDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(value = 1, message = MIN_MESSAGE) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
