package com.orderandwarehouse.app.integration;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.Type;
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

    private ComponentDto invalidComponentDto1;
    private ComponentDto invalidComponentDto2;
    private ComponentDto componentDto1;
    private ComponentDto componentDto2;
    private ComponentDto componentDto3;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/components";
        invalidComponentDto1 = ComponentDto.builder().name("").type(Type.SMD).build();
        invalidComponentDto2 = ComponentDto.builder().name("1".repeat(121)).type(Type.SMD).build();
        componentDto1 = ComponentDto.builder().name("component1").type(Type.SMD).build();
        componentDto2 = ComponentDto.builder().name("component2").type(Type.SMD).build();
        componentDto3 = ComponentDto.builder().name("component3").type(Type.THD).build();
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
    void emptyDatabase_addInvalidComponent01_returnsBAD_REQUEST() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(invalidComponentDto1);
        ResponseEntity<Object> result = restTemplate.postForEntity(entityUrl, request, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("name", "Name must not be blank!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    void emptyDatabase_addInvalidComponent02_returnsBAD_REQUEST() {
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
    void emptyDatabase_getByInvalidId_returnsBAD_REQUEST() {
        Long id = 0L;
        Map<String, String> expectedBody = new HashMap<>() {{
            put(id.toString(), "nagyobbnak, vagy egyenlőnek kell lennie, mint 1");
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

    @Test
    void oneComponentStored_wrongIdInUrlUpdateRequest_returnsBAD_REQUESTr() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(componentDto1);
        Component component1 = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, Component.class).getBody());
        componentDto1.setId(component1.getId());
        componentDto1.setName("componentDto1 updated");
        Long wrongId = 2L;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ComponentDto> httpEntity = new HttpEntity<>(componentDto1, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + wrongId, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Id in path doesn't match with Id in Body!", response.getBody());
    }

    @Test
    void oneComponentStored_noIdInDtoUpdateRequest_returnsBAD_REQUEST() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(componentDto1);
        Component component1 = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, Component.class).getBody());
        componentDto1.setName("componentDto1 updated");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ComponentDto> httpEntity = new HttpEntity<>(componentDto1, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + component1.getId(), HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Id in path doesn't match with Id in Body!", response.getBody());
    }

    @Test
    void oneComponentStored_wrongIdInDtoUpdateRequest_returnsError() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(componentDto1);
        Component component1 = Objects.requireNonNull(restTemplate.postForEntity(entityUrl, request, Component.class).getBody());
        Long wrongId = 2L;
        componentDto1.setId(wrongId);
        componentDto1.setName("componentDto1 updated");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ComponentDto> httpEntity = new HttpEntity<>(componentDto1, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + component1.getId(), HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Id in path doesn't match with Id in Body!", response.getBody());
    }

    @Test
    void someComponentsStored_deleteByValidId_getAllReturnsRemaining() {
        List<ComponentDto> testData = new ArrayList<>(List.of(componentDto1, componentDto2, componentDto3));
        List<Component> components = testData.stream().map(dto -> restTemplate.postForObject(entityUrl, dto, Component.class)).toList();
        int expected = testData.size();
        assertEquals(expected, components.size());

        testData.remove(componentDto1);
        Set<String> componentDtoNames = testData.stream().map(ComponentDto::getName).collect(Collectors.toSet());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<HttpStatus> deleteResponse = restTemplate.exchange(entityUrl + "/" + components.get(0).getId(), HttpMethod.DELETE, httpEntity, HttpStatus.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Component[]> getAllResponse = restTemplate.getForEntity(entityUrl, Component[].class);
        Component[] result = Objects.requireNonNull(getAllResponse.getBody());
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(expected - 1, result.length);
        assertTrue(Arrays.stream(result).map(Component::getName).allMatch(componentDtoNames::contains));
    }

    @Test
    void oneComponentStored_deleteByValidId_getAllReturnsEmptyList() {
        Component component = restTemplate.postForObject(entityUrl, componentDto1, Component.class);
        assertNotNull(component);
        assertEquals(1, component.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<HttpStatus> deleteResponse = restTemplate.exchange(entityUrl + "/" + component.getId(), HttpMethod.DELETE, httpEntity, HttpStatus.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Component[]> getAllResponse = restTemplate.getForEntity(entityUrl, Component[].class);
        Component[] result = Objects.requireNonNull(getAllResponse.getBody());
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(0, result.length);
    }

    @Test
    void oneComponentStored_deleteByNonExistingId_getAllReturnsNOT_FOUND() {
        Component component = restTemplate.postForObject(entityUrl, componentDto1, Component.class);
        assertNotNull(component);
        assertEquals(1, component.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(entityUrl + "/" + component.getId() + 1, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
        assertEquals("No value present", deleteResponse.getBody());

        ResponseEntity<Component[]> getAllResponse = restTemplate.getForEntity(entityUrl, Component[].class);
        Component[] result = Objects.requireNonNull(getAllResponse.getBody());
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(1, result.length);
        assertEquals(component.getId(), getAllResponse.getBody()[0].getId());
    }
}
