package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.service.PartsListRowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import static com.orderandwarehouse.app.util.Constants.MIN_MESSAGE;

@RestController
@RequestMapping("/products/partslist")
@RequiredArgsConstructor
@Validated
public class PartsListRowController {
    private final PartsListRowService service;

    @Operation(
            summary = "Gets the partsList of the product with the given Id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the parts list of the requested product."),
                    @ApiResponse(responseCode = "404", description = "The requested Product was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID < 1")
            }
    )
    @GetMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> getPartsListByProductId(@PathVariable @Min(value = 1, message = MIN_MESSAGE) Long productId) {
        return new ResponseEntity<>(service.getPartsListByProductId(productId), HttpStatus.OK);
    }

    @Operation(
            summary = "Adds 1 new line to a product's parts list. PartsListRow is given as Json object in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the new PartsListRow entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: component quantity < 0."),
                    @ApiResponse(responseCode = "404", description = "Product or Component is not found")
            }
    )
    @PostMapping
    public ResponseEntity<PartsListRow> add(@RequestBody @Valid PartsListRowDto dto) {
        return new ResponseEntity<>(service.add(dto), HttpStatus.OK);
    }

    @Operation(
            summary = "Adds multiple new lines to a product's parts list. PartsListRows are given as a List of Json objects in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returns with the new PartsListRow entities."),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request, for eg.: productId in path doesn't match with the productId in partsListRows."),
                    @ApiResponse(responseCode = "404",
                            description = "Product or Component is not found")
            }
    )
    @PostMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> addAllToProduct(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long productId,
                                                              @RequestBody @Valid List<PartsListRowDto> partsList) {
        return new ResponseEntity<>(service.addAllToProduct(productId, partsList), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the whole parts list of a product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the updated parts list."),
                    @ApiResponse(responseCode = "404", description = "The Product was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "Product has an active order,the parts list can't be modified.")
            }
    )
    @PutMapping("/{productId}")
    public ResponseEntity<List<PartsListRow>> updateAllBelongingToProduct(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long productId,
                                                                          @RequestBody @Valid Set<PartsListRowDto> partsList) {
        return new ResponseEntity<>(service.updateAllBelongingToProduct(productId, partsList), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the PartsListRow with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "PartsListRow has been successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The PartsListRow was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "Product has an active order,the partsListRow can't be deleted.")
            }
    )
    @DeleteMapping("/delete/single/row/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the whole parts list of a product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parts list has been successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The Product was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "Product has an active order,the parts list can't be deleted.")
            }
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpStatus> deleteAllByProductId(@PathVariable @Min(value = 1, message = MIN_MESSAGE) Long productId) {
        service.deleteAllByProductId(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
