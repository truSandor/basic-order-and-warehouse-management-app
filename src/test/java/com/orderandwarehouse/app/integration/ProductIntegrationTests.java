package com.orderandwarehouse.app.integration;


import com.orderandwarehouse.app.controller.PartsListRowController;
import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.Type;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ProductIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PartsListRowController partsListRowController;

    @LocalServerPort
    private int port;
    private String entityUrl;
    private String baseUrl;

    private Component component1;

    private ProductDto productDto1;
    private ProductDto productDto2;
    private ProductDto productDto3;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/products";

        productDto1 = ProductDto.builder().name("product1").build();
        productDto2 = ProductDto.builder().name("product2").build();
        productDto3 = ProductDto.builder().name("product3").build();

        ComponentDto componentDto1 = ComponentDto.builder().name("component1").type(Type.SMD).build();
        component1 = restTemplate.postForObject(baseUrl + "/components", componentDto1, Component.class);
        component1.setPartsListRows(List.of());
        component1.setStorageUnits(List.of());
    }

    @Test
    void emptyDatabase_getAll_returnsEmpty() {
        assertEquals(Collections.emptyList(), List.of(restTemplate.getForObject(entityUrl, Product[].class)));
    }

    @Test
    void emptyDataBase_addOneProduct_returnsAddedProduct() {
        ResponseEntity<Product> response = restTemplate.postForEntity(entityUrl, productDto1, Product.class);
        Product product1 = Objects.requireNonNull(response.getBody());
        assertEquals(productDto1.getName(), product1.getName());
    }

    @Test
    void twoProductsInDataBase_getAll_returnsTwoProduct() {
        Product product1 = restTemplate.postForObject(entityUrl, productDto1, Product.class);
        Product product2 = restTemplate.postForObject(entityUrl, productDto2, Product.class);
        product1.setPartsList(List.of());
        product1.setOrders(List.of());
        product2.setPartsList(List.of());
        product2.setOrders(List.of());
        assertEquals(List.of(product1, product2), List.of(restTemplate.getForObject(entityUrl, Product[].class)));
    }

    @Test
    void emptyDataBase_addProductWithInvalidName01_returnsBAD_REQUEST() {
        productDto1.setName(null);
        ResponseEntity<Object> response = restTemplate.postForEntity(entityUrl, productDto1, Object.class);
        Object result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedBody = new HashMap<>() {{
            put("name", "Name must not be blank!");
        }};
        assertEquals(expectedBody, result);
        assertEquals(Collections.emptyList(), List.of(restTemplate.getForObject(entityUrl, Product[].class)));
    }

    @Test
    void emptyDataBase_addProductWithInvalidName02_returnsBAD_REQUEST() {
        productDto1.setName("");
        ResponseEntity<Object> response = restTemplate.postForEntity(entityUrl, productDto1, Object.class);
        Object result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedBody = new HashMap<>() {{
            put("name", "Name must not be blank!");
        }};
        assertEquals(expectedBody, result);
    }

    @Test
    void emptyDataBase_addProductWithInvalidName03_returnsBAD_REQUEST() {
        productDto1.setName("1".repeat(121));
        ResponseEntity<Object> response = restTemplate.postForEntity(entityUrl, productDto1, Object.class);
        Object result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedBody = new HashMap<>() {{
            put("name", "Max 120 characters!");
        }};
        assertEquals(expectedBody, result);
    }

    @Test
    void OneProductInDataBase_updateWithValidRequest_productUpdates() {
        Product product1 = restTemplate.postForObject(entityUrl, productDto1, Product.class);
        LocalDateTime dateAdded = product1.getDateAdded();
        LocalDateTime dateModified = product1.getDateModified();
        productDto1.setId(product1.getId());
        productDto1.setName("product1 updates");
        productDto1.setVersion("v1.0");
        productDto1.setWeightInGrammes(100);
        productDto1.setDimensions("30x30x30");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(productDto1, headers);
        ResponseEntity<Product> response = restTemplate.exchange(entityUrl + "/" + productDto1.getId(), HttpMethod.PUT, httpEntity, Product.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Product result = Objects.requireNonNull(response.getBody());
        assertEquals(productDto1.getId(), result.getId());
        assertEquals(productDto1.getName(), result.getName());
        assertEquals(productDto1.getVersion(), result.getVersion());
        assertEquals(productDto1.getWeightInGrammes(), result.getWeightInGrammes());
        assertEquals(productDto1.getDimensions(), result.getDimensions());
        assertEquals(dateAdded.truncatedTo(ChronoUnit.MILLIS), result.getDateAdded().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(result.getDateModified().isAfter(dateModified.truncatedTo(ChronoUnit.MILLIS)));
    }

    @Test
    void OneProductInDataBase_updateWithInValidRequest_returnsBAD_REQUEST() {
        Long wrongId = 200L;
        Product product1 = restTemplate.postForObject(entityUrl, productDto1, Product.class);
        productDto1.setId(wrongId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(productDto1, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + product1.getId(), HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Id in path doesn't match with Id in Body!", response.getBody());
    }

    @Test
    void OneProductInDataBase_updateWithNonExistentID_returnsNOT_FOUND() {
        Long wrongId = 200L;
        productDto1.setId(wrongId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(productDto1, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + productDto1.getId(), HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No value present", response.getBody());
    }

    @Test
    void someProductsWithoutPartsListStored_validDeleteRequest_getAllReturnsRemaining() {
        List<ProductDto> testData = new ArrayList<>(List.of(productDto1, productDto2, productDto3));
        List<Product> products = testData.stream()
                .map(dto -> {
                    Product p = restTemplate.postForObject(entityUrl, dto, Product.class);
                    dto.setId(p.getId());
                    return p;
                })
                .toList();
        int expected = testData.size();
        assertEquals(expected, products.size());
        assertNotNull(productDto1.getId());

        testData.remove(productDto1);
        Set<Long> productDtoIds = testData.stream().map(ProductDto::getId).collect(Collectors.toSet());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<HttpStatus> deleteResponse = restTemplate.exchange(entityUrl + "/" + productDto1.getId(), HttpMethod.DELETE, httpEntity, HttpStatus.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Product[]> getAllResponse = restTemplate.getForEntity(entityUrl, Product[].class);
        Product[] result = Objects.requireNonNull(getAllResponse.getBody());
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(expected - 1, result.length);
        assertTrue(Arrays.stream(result).map(Product::getId).allMatch(productDtoIds::contains));
    }

    @Test
    void oneProductStored_deleteByNonExistingId_getAllReturnsNOT_FOUND() {
        Long wrongId = 200L;
        restTemplate.postForObject(entityUrl, productDto1, Product.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + wrongId, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Record not found!", response.getBody());
    }

    @Test
    void oneProductWithPartListStored_deleteValidRequest_returnsNOT_ACCEPTABLE() {
        Product product = restTemplate.postForObject(entityUrl, productDto1, Product.class);
        PartsListRowDto partsListRowDto1 = PartsListRowDto.builder().productId(product.getId()).componentId(component1.getId()).quantity(2D).build();
        partsListRowController.add(partsListRowDto1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(entityUrl + "/" + product.getId(), HttpMethod.DELETE, httpEntity, Object.class);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Map<String, String> expectedBody = new HashMap<>() {{
            put("hasPartsList", "true");
            put("productId", "1");
            put("orderIds", "[]");
            put("message", "Product '1' has active orders or parts list. Close the orders and delete parts list to proceed!");
        }};
        assertEquals(expectedBody, response.getBody());
    }
}
