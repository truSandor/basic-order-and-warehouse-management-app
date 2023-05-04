package com.orderandwarehouse.app.integration;

import com.orderandwarehouse.app.controller.PartsListRowController;
import com.orderandwarehouse.app.model.*;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.model.dto.ProductDto;
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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PartsListRowIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PartsListRowController partsListRowController;

    @LocalServerPort
    private int port;
    private String entityUrl;
    private String baseUrl;

    private Product product1;
    private Component component1;
    private Component component2;
    private Component component3;
    private PartsListRowDto partsListRowDto1;
    private PartsListRowDto partsListRowDto2;
    private PartsListRowDto partsListRowDto3;

    private List<PartsListRow> partsList;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/products/partslist";

        ProductDto productDto1 = ProductDto.builder().name("product1").build();
        product1 = restTemplate.postForObject(baseUrl + "/products", productDto1, Product.class);

        ComponentDto componentDto1 = ComponentDto.builder().name("component1").type(Type.SMD).build();
        ComponentDto componentDto2 = ComponentDto.builder().name("component2").type(Type.THD).build();
        ComponentDto componentDto3 = ComponentDto.builder().name("component3").type(Type.MECHANICAL).build();
        component1 = restTemplate.postForObject(baseUrl + "/components", componentDto1, Component.class);
        component2 = restTemplate.postForObject(baseUrl + "/components", componentDto2, Component.class);
        component3 = restTemplate.postForObject(baseUrl + "/components", componentDto3, Component.class);

        partsListRowDto1 = PartsListRowDto.builder().productId(product1.getId()).componentId(component1.getId()).quantity(11.0).build();
        partsListRowDto2 = PartsListRowDto.builder().productId(product1.getId()).componentId(component2.getId()).quantity(22.0).build();
        partsListRowDto3 = PartsListRowDto.builder().productId(product1.getId()).componentId(component3.getId()).quantity(33.0).build();
    }


}
