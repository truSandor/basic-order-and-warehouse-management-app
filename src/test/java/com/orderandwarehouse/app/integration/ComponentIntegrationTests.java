package com.orderandwarehouse.app.integration;

import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.orderandwarehouse.app.data.TestComponentDto.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void emptyDatabase_getAll_shouldReturnEmptyList() {
        assertEquals(Collections.emptyList(), List.of(restTemplate.getForObject(entityUrl, Component[].class)));
    }

    @Test
    void emptyDatabase_addOne_shouldReturnAddedApprentice() {
        HttpEntity<ComponentDto> request = new HttpEntity<>(component1);
        Component result = restTemplate.postForObject(entityUrl, request, Component.class);
        assertEquals(component1.getName(), result.getName());
        assertEquals(component1.getType(), result.getType());
        assertTrue(LocalDateTime.now().isAfter(result.getDateAdded()));
        assertTrue(LocalDateTime.now().minusSeconds(2).isBefore(result.getDateAdded()));
        assertTrue(LocalDateTime.now().isAfter(result.getDateModified()));
        assertTrue(LocalDateTime.now().minusSeconds(2).isBefore(result.getDateModified()));
    }
}
