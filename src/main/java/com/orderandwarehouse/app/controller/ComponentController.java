package com.orderandwarehouse.app.controller;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.service.ComponentService;
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

import static com.orderandwarehouse.app.util.Constants.MIN_MESSAGE;
import static com.orderandwarehouse.app.util.Constants.MAX_SIZE_MESSAGE;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class ComponentController {
    private final ComponentService service;

    @Operation(
            summary = "Gets all components.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of Component entities.")
            }
    )
    @GetMapping
    public ResponseEntity<List<Component>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Gets the component with the given ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the requested Component entity."),
                    @ApiResponse(responseCode = "404", description = "The requested Component was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID < 1")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Component> getById(@PathVariable @Min(value = 1, message = MIN_MESSAGE) Long id) {
        return service.getById(id)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Adds new component to the database. Component is given as Json object in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the new Component entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: given name is blank")
            }
    )
    @PostMapping
    public ResponseEntity<Component> add(@RequestBody @Valid ComponentDto componentDto) {
        return new ResponseEntity<>(service.add(componentDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the component with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns with the updated Component entity."),
                    @ApiResponse(responseCode = "400", description = "Bad request, for eg.: ID in path is different from ID in body."),
                    @ApiResponse(responseCode = "404", description = "Not found in the database.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Component> update(@PathVariable @NotNull @Min(value = 1, message = MIN_MESSAGE) Long id,
                                            @RequestBody @Valid ComponentDto componentDto) {
        if (!id.equals(componentDto.getId()))
            throw new InputMismatchException("Id in path doesn't match with Id in Body!");
        return new ResponseEntity<>(service.update(componentDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the component with the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Component has been successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The Component was not found in the database."),
                    @ApiResponse(responseCode = "400", description = "Bad request, ID< 1."),
                    @ApiResponse(responseCode = "406", description = "Component is still in a PartsList, can't be deleted.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(value = 1, message = MIN_MESSAGE) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Gets all components which name is containing the given phrase.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with a list of Component entities."),
                    @ApiResponse(responseCode = "400", description = "Bad request, search phrase is too long.")
            }
    )
    @GetMapping(params = "nameLike")
    public ResponseEntity<List<Component>> getByNameLike(@RequestParam @Size(max = 40, message = MAX_SIZE_MESSAGE) String nameLike) {
        return new ResponseEntity<>(service.getByNameLike(nameLike), HttpStatus.OK);
    }
}
