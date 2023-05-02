package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Gets all orders.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of Order entities.")
            }
    )
    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Gets all orders where the product's name contains the given phrase.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of Order entities."),
                    @ApiResponse(responseCode = "400", description = "Bad request, search phrase is too long.")
            }
    )
    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Order>> getByProductNameLike(@RequestParam @Size(max = 40, message = MAX_SIZE_MESSAGE) String nameLike) {
        return new ResponseEntity<>(service.getByProductNameLike(nameLike), HttpStatus.OK);
    }

    @Operation(
            summary = "Gets the order with the given ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the requested Order entity."),
                    @ApiResponse(responseCode = "404", description = "The requested Order was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID < 1")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        return service.getById(id)
                .map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Adds new order to the database. Order is given as Json object in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the new Order entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: no productId is given.")
            }
    )
    @PostMapping
    public ResponseEntity<Order> add(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(service.add(orderDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the order with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the updated Order entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID in path is different from ID in body."),
                    @ApiResponse(responseCode = "404", description = "Not found in the database.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id, @RequestBody @Valid OrderDto orderDto) {
        if (!id.equals(orderDto.getId())) throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(orderDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the order with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order has been successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The Order was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "Order is in progress, can't be deleted.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(value = 1, message = MIN_MESSAGE) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
