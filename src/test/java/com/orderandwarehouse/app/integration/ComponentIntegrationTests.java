package com.orderandwarehouse.app.integration;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
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

import static com.orderandwarehouse.app.data.TestComponentDto.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ComponentIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;
    private String entityUrl;
    private String baseUrl;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/components";
    }

    @Test
    void emptyDatabase_getAll_returnEmptyList() {
        assertEquals(Collections.emptyList(), List.of(restTemplate.getForObject(entityUrl, Component[].class)));
    }

    @Test
    void emptyDatabase_addValidComponent_returnsAddedComponent() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(componentDto1);
        Component result = restTemplate.postForObject(entityUrl, request, Component.class);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals(componentDto1.getName(), result.getName());
        assertEquals(componentDto1.getType(), result.getType());
        assertTrue(LocalDateTime.now().isAfter(result.getDateAdded()));
        assertTrue(LocalDateTime.now().minusSeconds(2).isBefore(result.getDateAdded()));
        assertTrue(LocalDateTime.now().isAfter(result.getDateModified()));
        assertTrue(LocalDateTime.now().minusSeconds(2).isBefore(result.getDateModified()));
    }

    @Test
    void emptyDatabase_addInvalidComponent01_returnsError() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(invalidComponentDto1);
        ResponseEntity<Object> result = restTemplate.postForEntity(entityUrl, request, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("name", "Name must not be blank!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    void emptyDatabase_addInvalidComponent02_returnsError() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(invalidComponentDto2);
        ResponseEntity<Object> result = restTemplate.postForEntity(entityUrl, request, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("name", "Max 120 characters!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    void someComponentStored_getAll_returnsAll() {
        Set<ComponentDto> testData = Set.of(componentDto1, componentDto2, componentDto3);
        testData.forEach(data -> restTemplate.postForObject(entityUrl, data, Component.class));
        Set<String> componentDtoNames = testData.stream().map(ComponentDto::getName).collect(Collectors.toSet());
        ResponseEntity<Component[]> response = restTemplate.getForEntity(entityUrl, Component[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Component[] result = Objects.requireNonNull(response.getBody());
        assertTrue(Arrays.stream(result).map(Component::getName).allMatch(componentDtoNames::contains));
    }

    @Test
    void oneComponentStored_getById_returnsComponent() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(componentDto1);
        Long id = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, Component.class).getBody()).getId();
        ResponseEntity<Component> response = restTemplate.getForEntity(entityUrl + "/" + id, Component.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Component result = Objects.requireNonNull(response.getBody());
        assertEquals(componentDto1.getName(), result.getName());
    }

    @Test
    void emptyDatabase_getByInvalidId_returnsError() {
        Long id = 0L;
        Map<String, String> expectedBody = new HashMap<>() {{
            put(id.toString(), "nagyobbnak, vagy egyenlőnek kell lennie, mint 1");
        }};
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "/" + id, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void emptyDatabase_getByNonExistentId_returnsNotFound() {
        Long id = 1L;
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "/" + id, Object.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void oneComponentStored_validUpdateRequest_returnsUpdatedComponent() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(componentDto1);
        Component component1 = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, Component.class).getBody());
        componentDto1.setId(component1.getId());
        componentDto1.setName("componentDto1 updated");
        LocalDateTime dateAdded = component1.getDateAdded();
        LocalDateTime dateModified = component1.getDateModified();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ComponentDto> httpEntity = new HttpEntity<>(componentDto1, headers);
        ResponseEntity<Component> response = restTemplate.exchange(entityUrl + "/" + componentDto1.getId(), HttpMethod.PUT, httpEntity, Component.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Component result = Objects.requireNonNull(response.getBody());
        assertEquals(componentDto1.getId(), result.getId());
        assertEquals(componentDto1.getName(), result.getName());
        assertEquals(dateAdded.truncatedTo(ChronoUnit.MILLIS), result.getDateAdded().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(result.getDateModified().isAfter(dateModified.truncatedTo(ChronoUnit.MILLIS)));
    }

}
