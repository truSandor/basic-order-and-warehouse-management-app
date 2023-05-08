package com.orderandwarehouse.app.integration;


import com.orderandwarehouse.app.controller.StorageUnitController;
import com.orderandwarehouse.app.exception.StorageUnitStillInUseException;
import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.StorageUnit;
import com.orderandwarehouse.app.model.Type;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StorageUnitIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StorageUnitController storageUnitController;


    @LocalServerPort
    private int port;
    private String entityUrl;
    private String baseUrl;

    private ComponentDto componentDto1;
    private Component component1;

    private StorageUnitDto storageUnitDto1;
    private StorageUnitDto storageUnitDto2;
    private StorageUnitDto storageUnitDto3;
    private StorageUnitDto invalidStorageUnitDto1;
    private StorageUnitDto invalidStorageUnitDto2;
    private StorageUnitDto invalidStorageUnitDto3;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/storage";

        componentDto1 = ComponentDto.builder().name("component1").type(Type.SMD).build();
        component1 = restTemplate.postForObject(baseUrl + "/components", componentDto1, Component.class);

        storageUnitDto1 = StorageUnitDto.builder().row(1).column(1).shelf(1).build();
        storageUnitDto2 = StorageUnitDto.builder().row(1).column(1).shelf(2).build();
        storageUnitDto3 = StorageUnitDto.builder().row(1).column(1).shelf(3).build();

        invalidStorageUnitDto1 = StorageUnitDto.builder().row(0).column(1).shelf(4).build();
        invalidStorageUnitDto2 = StorageUnitDto.builder().row(1).column(1).shelf(4).componentId(0L).build();
        invalidStorageUnitDto3 = StorageUnitDto.builder().row(1).column(1).shelf(4).quantity(-1.0).build();
    }

    @Test
    void emptyDatabase_getAll_returnEmptyList() {
        assertEquals(Collections.emptyList(), List.of(restTemplate.getForObject(entityUrl, Component[].class)));
    }

    @Test
    void emptyDatabase_addValidStorageUnit_returnsAddedStorageUnit() {
        HttpEntity<StorageUnitDto> request = new HttpEntity<>(storageUnitDto1);
        StorageUnit result = restTemplate.postForObject(entityUrl, request, StorageUnit.class);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals(storageUnitDto1.getRow(), result.getRow());
        assertEquals(storageUnitDto1.getColumn(), result.getColumn());
        assertEquals(storageUnitDto1.getShelf(), result.getShelf());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), result.getDateAdded().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), result.getDateModified().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void emptyDatabase_addInvalidStorageUnitDto1_returnsBAD_REQUEST() {
        HttpEntity<StorageUnitDto> request = new HttpEntity<>(invalidStorageUnitDto1);
        ResponseEntity<Object> result = restTemplate.postForEntity(entityUrl, request, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("row", "Needs to be greater or equal to 1!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    void emptyDatabase_addInvalidStorageUnitDto2_returnsBAD_REQUEST() {
        HttpEntity<StorageUnitDto> request = new HttpEntity<>(invalidStorageUnitDto2);
        ResponseEntity<Object> result = restTemplate.postForEntity(entityUrl, request, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("componentId", "Needs to be greater or equal to 1!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    void emptyDatabase_addInvalidStorageUnitDto3_returnsBAD_REQUEST() {
        HttpEntity<StorageUnitDto> request = new HttpEntity<>(invalidStorageUnitDto3);
        ResponseEntity<Object> result = restTemplate.postForEntity(entityUrl, request, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("quantity", "Needs to be greater or equal to 0.0!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    void someStorageUnitsStored_getAll_returnsAll() {
        List<StorageUnitDto> testData = List.of(storageUnitDto1, storageUnitDto2, storageUnitDto3);
        testData.forEach(data -> restTemplate.postForObject(entityUrl, data, Component.class));
        List<Integer> storageUnitDtoRows = testData.stream().map(StorageUnitDto::getRow).toList();
        List<Integer> storageUnitDtoColumns = testData.stream().map(StorageUnitDto::getColumn).toList();
        List<Integer> storageUnitDtoShelves = testData.stream().map(StorageUnitDto::getShelf).toList();
        ResponseEntity<StorageUnit[]> response = restTemplate.getForEntity(entityUrl, StorageUnit[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StorageUnit[] result = Objects.requireNonNull(response.getBody());
        assertEquals(storageUnitDtoRows, Arrays.stream(result).map(StorageUnit::getRow).toList());
        assertEquals(storageUnitDtoColumns, Arrays.stream(result).map(StorageUnit::getColumn).toList());
        assertEquals(storageUnitDtoShelves, Arrays.stream(result).map(StorageUnit::getShelf).toList());
        assertEquals(List.of(1L, 2L, 3L), Arrays.stream(result).map(StorageUnit::getId).toList());
    }

    @Test
    void oneStorageUnitStored_getById_returnsStorageUnit() {
        HttpEntity<StorageUnitDto> request = new HttpEntity<>(storageUnitDto1);
        Long id = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, StorageUnit.class).getBody()).getId();
        ResponseEntity<StorageUnit> response = restTemplate.getForEntity(entityUrl + "/" + id, StorageUnit.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StorageUnit result = Objects.requireNonNull(response.getBody());
        assertEquals(storageUnitDto1.getRow(), result.getRow());
        assertEquals(storageUnitDto1.getColumn(), result.getColumn());
        assertEquals(storageUnitDto1.getShelf(), result.getShelf());
    }

    @Test
    void emptyDatabase_getByInvalidId_returnsBAD_REQUEST() {
        Long id = 0L;
        Map<String, String> expectedBody = new HashMap<>() {{
            put(id.toString(), "Needs to be greater or equal to 1!");
        }};
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "/" + id, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }


    @Test
    void emptyDatabase_getByNonExistentId_returnsNOT_FOUND() {
        Long id = 1L;
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "/" + id, Object.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void oneEmptyStorageUnitStored_validUpdateRequest_returnsUpdatedStorageUnit() {
        HttpEntity<StorageUnitDto> request = new HttpEntity<>(storageUnitDto1);
        StorageUnit storageUnit1 = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, StorageUnit.class).getBody());
        storageUnitDto1.setId(storageUnit1.getId());
        storageUnitDto1.setComponentId(component1.getId());
        storageUnitDto1.setQuantity(100.0);
        LocalDateTime dateAdded = storageUnit1.getDateAdded();
        LocalDateTime dateModified = storageUnit1.getDateModified();

        ResponseEntity<StorageUnit> response = storageUnitController.update(storageUnit1.getId(), storageUnitDto1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StorageUnit result = Objects.requireNonNull(response.getBody());
        assertEquals(storageUnit1.getId(), result.getId());
        assertEquals(storageUnitDto1.getComponentId(), result.getComponent().getId());
        assertEquals(dateAdded.truncatedTo(ChronoUnit.MILLIS), result.getDateAdded().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(dateModified.isBefore(result.getDateModified()));
        assertEquals(storageUnitDto1.getQuantity(), result.getQuantity());
    }

    @Test
    void oneOccupiedStorageUnitStored_validUpdateRequest_returnsUpdatedStorageUnit() {
        storageUnitDto1.setComponentId(component1.getId());
        storageUnitDto1.setQuantity(100.0);
        StorageUnit storageUnit1 = Objects.requireNonNull(storageUnitController.add(storageUnitDto1).getBody());
        storageUnitDto1.setId(storageUnit1.getId());
        LocalDateTime dateAdded = storageUnit1.getDateAdded();
        LocalDateTime dateModified = storageUnit1.getDateModified();
        storageUnitDto1.setComponentId(null);
        storageUnitDto1.setQuantity(0.0);

        ResponseEntity<StorageUnit> response = storageUnitController.update(storageUnit1.getId(), storageUnitDto1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StorageUnit result = Objects.requireNonNull(response.getBody());
        assertEquals(storageUnit1.getId(), result.getId());
        assertNull(result.getComponent());
        assertEquals(0.0, result.getQuantity());
        assertEquals(dateAdded.truncatedTo(ChronoUnit.MILLIS), result.getDateAdded().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(dateModified.isBefore(result.getDateModified()));
        assertEquals(storageUnitDto1.getQuantity(), result.getQuantity());
    }

    @Test
    void someEmptyStorageUnitStored_validDeleteRequest_getAllReturnsRemaining() {
        List<StorageUnitDto> testData = new ArrayList<>(List.of(storageUnitDto1, storageUnitDto2, storageUnitDto3));
        List<StorageUnit> storageUnits = testData.stream()
                .map(dto -> {
                    StorageUnit su = restTemplate.postForObject(entityUrl, dto, StorageUnit.class);
                    dto.setId(su.getId());
                    return su;
                })
                .toList();
        int expected = testData.size();
        assertEquals(expected, storageUnits.size());
        assertNotNull(storageUnitDto1.getId());

        testData.remove(storageUnitDto1);
        Set<Long> storageUnitDtoIds = testData.stream().map(StorageUnitDto::getId).collect(Collectors.toSet());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<HttpStatus> deleteResponse = restTemplate.exchange(entityUrl + "/" + storageUnitDto1.getId(), HttpMethod.DELETE, httpEntity, HttpStatus.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<StorageUnit[]> getAllResponse = restTemplate.getForEntity(entityUrl, StorageUnit[].class);
        StorageUnit[] result = Objects.requireNonNull(getAllResponse.getBody());
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(expected - 1, result.length);
        assertTrue(Arrays.stream(result).map(StorageUnit::getId).allMatch(storageUnitDtoIds::contains));
    }

    @Test
    void oneOccupiedStorageUnitStored_validDeleteRequest_returnsNOT_ACCEPTABLE() {
        List<StorageUnitDto> testData = new ArrayList<>(List.of(storageUnitDto1, storageUnitDto2, storageUnitDto3));
        storageUnitDto1.setComponentId(component1.getId());
        List<StorageUnit> storageUnits = testData.stream()
                .map(dto -> {
                    StorageUnit su = Objects.requireNonNull(storageUnitController.add(dto).getBody());
                    dto.setId(su.getId());
                    return su;
                })
                .toList();
        int expected = testData.size();
        assertEquals(expected, storageUnits.size());
        assertNotNull(storageUnitDto1.getId());
        assertThrows(StorageUnitStillInUseException.class, () -> storageUnitController.delete(storageUnitDto1.getId()));

        ResponseEntity<List<StorageUnit>> getAllResponse = storageUnitController.getAll();
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        storageUnits = Objects.requireNonNull(getAllResponse.getBody());
        assertEquals(expected, storageUnits.size());
    }

    @Test
    void SomeOccupiedStorageUnitsStored_getAllByComponentId_returnsWithListOfStorageUnitsContainingTheComponent() {
        ComponentDto componentDto2 = ComponentDto.builder().name("component2").type(Type.THD).build();
        Component component2 = restTemplate.postForObject(baseUrl + "/components", componentDto2, Component.class);
        storageUnitDto1.setComponentId(component2.getId());
        storageUnitDto2.setComponentId(component1.getId());
        storageUnitDto3.setComponentId(component2.getId());
        storageUnitController.add(storageUnitDto1);
        storageUnitController.add(storageUnitDto2);
        storageUnitController.add(storageUnitDto3);

        ResponseEntity<List<StorageUnit>> response = storageUnitController.getAllByComponentId(component2.getId());
        List<StorageUnit> result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(su -> su.getComponent().getId().equals(component2.getId())));
    }
}
