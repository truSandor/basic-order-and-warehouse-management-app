package com.orderandwarehouse.app.integration;


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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StorageUnitIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

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
}
