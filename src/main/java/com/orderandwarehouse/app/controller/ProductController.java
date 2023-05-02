package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;
import com.orderandwarehouse.app.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class ProductController {
    private final ProductService service;

    @Operation(
            summary = "Gets all products.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of Product entities.")
            }
    )
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Gets the product with the given ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the requested Product entity."),
                    @ApiResponse(responseCode = "404", description = "The requested Product was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID < 1")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        return service.getById(id)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Gets all products which name is containing the given phrase.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of Product entities."),
                    @ApiResponse(responseCode = "400", description = "Bad request, search phrase is too long."),
            }
    )
    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Product>> getByNameLike(@RequestParam @Size(max = 40, message = MAX_SIZE_MESSAGE) String nameLike) {
        return new ResponseEntity<>(service.getByNameLike(nameLike), HttpStatus.OK);
    }

    @Operation(
            summary = "Adds new product to the database. Product is given as Json object in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the new Product entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: given name is blank")
            }
    )
    @PostMapping
    public ResponseEntity<Product> add(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(service.add(productDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the product with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the updated Product entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID in path is different from ID in body."),
                    @ApiResponse(responseCode = "404", description = "Not found in the database.")
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<Product> update(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id,
                                          @RequestBody @Valid ProductDto productDto) {
        if (!id.equals(productDto.getId()))
            throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(productDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the product with the given ID, if it doesn't have active order, and it's parts list is empty.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product has been successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The Product was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "Product still in has parts list or active order, can't be deleted.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
