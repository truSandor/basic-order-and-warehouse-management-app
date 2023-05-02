package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import com.orderandwarehouse.app.service.StorageUnitService;
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

import java.util.InputMismatchException;
import java.util.List;

import static com.orderandwarehouse.app.util.Constants.MIN_MESSAGE;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class StorageUnitController {
    private final StorageUnitService service;

    @Operation(
            summary = "Gets all storage units.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of StorageUnit entities.")
            }
    )
    @GetMapping
    public ResponseEntity<List<StorageUnit>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Gets the storage unit with the given ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the requested Storage Unit entity."),
                    @ApiResponse(responseCode = "404", description = "The requested Storage Unit was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID < 1")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StorageUnit> getById(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        return service.getById(id)
                .map(su -> new ResponseEntity<>(su, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Adds new storage unit to the database. Storage unit is given as Json object in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the new StorageUnit entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: no row/column/shelf number is given.")
            }
    )
    @PostMapping
    public ResponseEntity<StorageUnit> add(@RequestBody @Valid StorageUnitDto storageUnitDto) {
        return new ResponseEntity<>(service.add(storageUnitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the storage unit with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the updated StorageUnit entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: component quantity is < 0."),
                    @ApiResponse(responseCode = "404", description = "Not found in the database.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<StorageUnit> update(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id,
                                              @RequestBody @Valid StorageUnitDto storageUnitDto) {
        if (!id.equals(storageUnitDto.getId()))
            throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(storageUnitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the storage unit with the given ID, if it's empty",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Storage unit has been successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The storage unit was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "storage unit is still in use, can't be deleted.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Gets all storage units which contains the given Component. (component is given with id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of StorageUnit entities."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID<1."),
                    @ApiResponse(responseCode = "404", description = "Component not found!")
            }
    )
    @GetMapping("/component/{component_id}")
    public ResponseEntity<List<StorageUnit>> getAllByComponentId(@PathVariable(name = "component_id")
                                                                 @NotNull
                                                                 @Min(value = 1, message = MIN_MESSAGE)
                                                                 Long componentId) {
        return new ResponseEntity<>(service.getAllByComponentId(componentId), HttpStatus.OK);
    }
}
