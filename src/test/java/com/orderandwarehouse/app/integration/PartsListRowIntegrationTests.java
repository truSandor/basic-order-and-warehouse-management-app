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

import java.util.*;
import java.util.stream.Collectors;

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

        product1.setPartsList(List.of());
        product1.setOrders(List.of());
        component1.setPartsListRows(List.of());
        component1.setStorageUnits(List.of());

    }


    @Test
    public void oneProductNoPartsList_getPartsListByProductId_returnEmptyList() {
        ResponseEntity<PartsListRow[]> response = restTemplate.getForEntity(entityUrl + "/" + product1.getId(), PartsListRow[].class);
        List<PartsListRow> result = List.of(Objects.requireNonNull(response.getBody()));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void oneProductNoPartsList_getPartsListByInvalidProductId_returnBAD_REQUEST() {
        final long invalidId = 0;
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "/" + invalidId, Object.class);
        Object result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedBody = new HashMap<>() {{
            put(String.valueOf(invalidId), "Needs to be greater or equal to 1!");
        }};
        assertEquals(expectedBody, result);
    }

    @Test
    public void oneProductNoPartsList_getPartsListByWrongProductId_returnNOT_FOUND() {
        final long wrongId = 2;
        ResponseEntity<String> response = restTemplate.getForEntity(entityUrl + "/" + wrongId, String.class);
        String result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String expectedBody = String.format("Product '%d' not found!", wrongId);
        assertEquals(expectedBody, result);
    }

    @Test
    public void oneProductNoPartsList_addOneValidPartsListRow_returnsPartsListRow() {
        ResponseEntity<PartsListRow> response = partsListRowController.add(partsListRowDto1);
        PartsListRow result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, result.getId());
        assertEquals(product1, result.getProduct());
        assertEquals(component1, result.getComponent());
        assertEquals(partsListRowDto1.getQuantity(), result.getQuantity());

    }

    @Test
    public void oneProductInDatabase_addValidPartsList_returnsAddedPartsListRows() {
        List<PartsListRowDto> partsListRowDtos = List.of(partsListRowDto1, partsListRowDto2, partsListRowDto3);
        ResponseEntity<List<PartsListRow>> response = partsListRowController.addAllToProduct(1L, partsListRowDtos);
        List<PartsListRow> result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<String> componentNames = Set.of(component1.getName(), component2.getName(), component3.getName());
        assertTrue(result.stream().allMatch(plr -> componentNames.contains(plr.getComponent().getName())));
        assertTrue(result.stream().allMatch(plr -> plr.getProduct().getName().equals(product1.getName())));

        ResponseEntity<List<PartsListRow>> response1 = partsListRowController.getPartsListByProductId(product1.getId());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        List<PartsListRow> partsList = Objects.requireNonNull(response1.getBody());
        assertTrue(partsList.stream().allMatch(plr -> componentNames.contains(plr.getComponent().getName())));
        assertTrue(partsList.stream().allMatch(plr -> plr.getProduct().getName().equals(product1.getName())));
    }

    @Test
    public void oneProductInDatabase_addPartsListRowWithInvalidProductId_returnsBAD_REQUEST() {
        partsListRowDto1.setProductId(0L);
        ResponseEntity<Object> response = restTemplate.postForEntity(entityUrl, partsListRowDto1, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("productId", "Needs to be greater or equal to 1!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void oneProductInDatabase_addPartsListRowWithNonExistentProductId_returnsNOT_FOUND() {
        partsListRowDto1.setProductId(200L);
        ResponseEntity<String> response = restTemplate.postForEntity(entityUrl, partsListRowDto1, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found!", response.getBody());
    }

    @Test
    public void oneProductInDatabase_addPartsListRowWithInvalidComponentId_returnsBAD_REQUEST() {
        partsListRowDto1.setComponentId(0L);
        ResponseEntity<Object> response = restTemplate.postForEntity(entityUrl, partsListRowDto1, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("componentId", "Needs to be greater or equal to 1!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void oneProductInDatabase_addPartsListRowWithNonExistentComponentId_returnsNOT_FOUND() {
        partsListRowDto1.setComponentId(200L);
        ResponseEntity<String> response = restTemplate.postForEntity(entityUrl, partsListRowDto1, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Component not found!", response.getBody());
    }

    @Test
    public void oneProductInDatabaseWithPartsLists_updateAllBelongingToProduct_returnsPartsList() {
        List<PartsListRowDto> partsListRowDtos = List.of(partsListRowDto1, partsListRowDto2, partsListRowDto3);
        ResponseEntity<List<PartsListRow>> responseEntity = partsListRowController.addAllToProduct(1L, partsListRowDtos);
        List<PartsListRow> partsList = Objects.requireNonNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(partsList.stream().unordered().noneMatch(plr -> plr.getQuantity() == 2000000000000.0));

        Double updatedQuantity = 2000000000000.0;
        partsListRowDto1.setQuantity(updatedQuantity);
        partsListRowDto2.setQuantity(updatedQuantity);
        partsListRowDto3.setQuantity(updatedQuantity);
        partsListRowDto1.setId(1L);
        partsListRowDto2.setId(2L);
        partsListRowDto3.setId(3L);

        ResponseEntity<List<PartsListRow>> response = partsListRowController.updateAllBelongingToProduct(1L, new HashSet<>(partsListRowDtos));
        List<PartsListRow> result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<String> componentNames = Set.of(component1.getName(), component2.getName(), component3.getName());
        assertTrue(result.stream().allMatch(plr -> componentNames.contains(plr.getComponent().getName())));
        assertTrue(result.stream().allMatch(plr -> plr.getProduct().getName().equals(product1.getName())));
        assertTrue(result.stream().allMatch(plr -> Objects.equals(plr.getQuantity(), updatedQuantity)));
    }

    @Test
    public void oneProductInDatabaseWithPartsLists_updatedPartsListContainsOneRowNotBelongingToProduct_returnsBAD_REQUEST() {
        List<PartsListRowDto> partsListRowDtos = List.of(partsListRowDto1, partsListRowDto2, partsListRowDto3);
        ResponseEntity<List<PartsListRow>> responseEntity = partsListRowController.addAllToProduct(1L, partsListRowDtos);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        partsListRowDto2.setProductId(2L);
        InputMismatchException ex = assertThrows(InputMismatchException.class,
                () -> partsListRowController.updateAllBelongingToProduct(1L, new HashSet<>(partsListRowDtos)));
        assertEquals("Not all parts list rows have the same product id, Or id in path is different!", ex.getMessage());
    }

    @Test
    public void oneProductInDatabaseWithPartsLists_deleteOneRow_deletesLine() {
        List<PartsListRowDto> partsListRowDtos = List.of(partsListRowDto1, partsListRowDto2, partsListRowDto3);
        ResponseEntity<List<PartsListRow>> responseEntity = partsListRowController.addAllToProduct(1L, partsListRowDtos);
        List<PartsListRow> partsList = Objects.requireNonNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        PartsListRow partsListRow = partsList.remove(0);
        Set<Long> partsListRowIds = partsList.stream().map(PartsListRow::getId).collect(Collectors.toSet());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<HttpStatus> deleteResponse = restTemplate.exchange(entityUrl + "/delete/single/row/" + partsListRow.getId(), HttpMethod.DELETE, httpEntity, HttpStatus.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<List<PartsListRow>> response1 = partsListRowController.getPartsListByProductId(product1.getId());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        List<PartsListRow> result = Objects.requireNonNull(response1.getBody());
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(plr -> partsListRowIds.contains(plr.getId())));
        assertFalse(result.stream().anyMatch(plr -> plr.getId().equals(partsListRow.getId())));
    }

    @Test
    public void oneProductInDatabaseWithPartsLists_deletePartsList_deletesPartsList() {
        List<PartsListRowDto> partsListRowDtos = List.of(partsListRowDto1, partsListRowDto2, partsListRowDto3);
        ResponseEntity<List<PartsListRow>> responseEntity = partsListRowController.addAllToProduct(1L, partsListRowDtos);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<HttpStatus> deleteResponse = restTemplate.exchange(entityUrl + "/" + product1.getId() , HttpMethod.DELETE, httpEntity, HttpStatus.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<List<PartsListRow>> response1 = partsListRowController.getPartsListByProductId(product1.getId());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
       assertTrue(Objects.requireNonNull(response1.getBody()).isEmpty());
    }
}
