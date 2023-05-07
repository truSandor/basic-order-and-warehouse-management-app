package com.orderandwarehouse.app.integration;

import com.orderandwarehouse.app.controller.OrderController;
import com.orderandwarehouse.app.model.*;
import com.orderandwarehouse.app.model.dto.OrderDto;
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
public class OrderIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private OrderController orderController;

    @LocalServerPort
    private int port;
    private String entityUrl;
    private String baseUrl;

    private Product product1;
    private Product product2;
    private Product product3;

    private OrderDto orderDto1;
    private OrderDto orderDto2;
    private OrderDto orderDto3;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/orders";

        ProductDto productDto1 = ProductDto.builder().name("product1 - relay board").build();
        ProductDto productDto2 = ProductDto.builder().name("product2 - gateway module").build();
        ProductDto productDto3 = ProductDto.builder().name("product3 - usb module").build();
        product1 = restTemplate.postForObject(baseUrl + "/products", productDto1, Product.class);
        product2 = restTemplate.postForObject(baseUrl + "/products", productDto2, Product.class);
        product3 = restTemplate.postForObject(baseUrl + "/products", productDto3, Product.class);

        orderDto1 = OrderDto.builder()
                .productId(product1.getId())
                .quantity(10)
                .dateReceived(LocalDateTime.parse("2023-01-03T23:51:02.250"))
                .status(Status.NOT_STARTED)
                .build();
        orderDto2 = OrderDto.builder()
                .productId(product2.getId())
                .quantity(20)
                .dateReceived(LocalDateTime.parse("2023-01-01T23:51:02.250"))
                .status(Status.IN_PROGRESS)
                .build();
        orderDto3 = OrderDto.builder()
                .productId(product3.getId())
                .quantity(30)
                .dateReceived(LocalDateTime.parse("2023-01-02T23:51:02.250"))
                .status(Status.COMPLETED)
                .build();
    }

    @Test
    void emptyDataBase_getAll_returnsEmptyList() {
        assertEquals(Collections.emptyList(), List.of(restTemplate.getForObject(entityUrl, Order[].class)));
    }

    @Test
    void emptyDataBase_addOneValidOrder_returnsOrder() {
        ResponseEntity<Order> response = orderController.add(orderDto1);
        Order result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto1.getProductId(), result.getProduct().getId());
        assertEquals(orderDto1.getQuantity(), result.getQuantity());
        assertEquals(orderDto1.getDateStarted(), result.getDateStarted());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), result.getDateAdded().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), result.getDateModified().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void addSomeOrders_getAll_ReturnsAllinDateReceivedOrder() {
        orderController.add(orderDto1);
        orderController.add(orderDto2);
        orderController.add(orderDto3);
        ResponseEntity<List<Order>> response = orderController.getAll();
        List<Order> result = Objects.requireNonNull(response.getBody());
        List<String> productNames = result.stream().map(o -> o.getProduct().getName()).toList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, result.size());
        assertEquals(List.of(product2.getName(), product3.getName(), product1.getName()), productNames);
    }

    @Test
    void addSomeOrders_getByValidId_ReturnsRequestedOrder() {
        orderController.add(orderDto1);
        Order order2 = Objects.requireNonNull(orderController.add(orderDto2).getBody());
        orderController.add(orderDto3);
        ResponseEntity<Order> response = orderController.getById(order2.getId());
        Order result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order2.getId(), result.getId());
        assertEquals(order2.getProduct().getId(), result.getProduct().getId());
        assertEquals(order2.getQuantity(), result.getQuantity());
        assertEquals(order2.getDateAdded(), result.getDateAdded());
        assertEquals(order2.getDateStarted(), result.getDateStarted());
    }

    @Test
    void emptyDataBase_getByInvalidId_ReturnsBAD_REQUEST() {
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "/-34", Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("-34", "Needs to be greater or equal to 1!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void emptyDataBase_addInvalidOrder_ReturnsBAD_REQUEST() {
        orderDto1.setProductId(null);
        ResponseEntity<Object> response = restTemplate.postForEntity(entityUrl, orderDto1, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("productId", "nem lehet null");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void emptyDataBase_addOrderWithNonexistentProductId_ReturnsNOT_FOUND() {
        orderDto1.setProductId(3000L);
        ResponseEntity<String> response = restTemplate.postForEntity(entityUrl, orderDto1, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No value present", response.getBody());
    }

    @Test
    void oneOrderStored_updateWithValidRequest_ReturnsUpdatedOrder() {
        Order order1 = Objects.requireNonNull(orderController.add(orderDto1).getBody());
        orderDto1.setId(order1.getId());
        orderDto1.setQuantity(3000);
        ResponseEntity<Order> response = orderController.update(order1.getId(), orderDto1);
        Order result = Objects.requireNonNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order1.getId(), result.getId());
        assertEquals(orderDto1.getProductId(), result.getProduct().getId());
        assertEquals(orderDto1.getQuantity(), result.getQuantity());
        assertEquals(order1.getDateAdded(), result.getDateAdded());
        assertTrue(order1.getDateModified().isBefore(result.getDateModified()));
    }

    @Test
    void oneOrderStored_updateWithInvalidRequest_ReturnsBAD_REQUEST() {
        Long wrongId = 2000L;
        Order order1 = Objects.requireNonNull(orderController.add(orderDto1).getBody());
        orderDto1.setId(wrongId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(orderDto1, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + order1.getId(), HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Id in path doesn't match with Id in Body!", response.getBody());
        Order orderInDB = Objects.requireNonNull(orderController.getById(order1.getId()).getBody());
        assertEquals(orderInDB.getDateAdded(), orderInDB.getDateModified());
    }

    @Test
    void multipleOrderStored_deleteTwoValidRequest_getAllReturnsRemaining() {
        List<OrderDto> testData = new ArrayList<>(List.of(orderDto1, orderDto2, orderDto3));
        List<Order> orders = testData.stream().map(o -> orderController.add(o).getBody()).toList();
        int expected = testData.size();
        assertEquals(expected, orders.size());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response1 = restTemplate.exchange(entityUrl + "/" + orders.get(2).getId(), HttpMethod.DELETE, httpEntity, Object.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<List<Order>> getAllResponse1 = orderController.getAll();
        List<Order> result1 = Objects.requireNonNull(getAllResponse1.getBody());
        assertEquals(HttpStatus.OK, getAllResponse1.getStatusCode());
        assertEquals(expected - 1, result1.size());
        assertFalse(result1.contains(orders.get(2)));

        ResponseEntity<Object> response2 = restTemplate.exchange(entityUrl + "/" + orders.get(0).getId(), HttpMethod.DELETE, httpEntity, Object.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        ResponseEntity<List<Order>> getAllResponse2 = orderController.getAll();
        List<Order> result2 = Objects.requireNonNull(getAllResponse2.getBody());
        assertEquals(HttpStatus.OK, getAllResponse2.getStatusCode());
        assertEquals(expected - 2, result2.size());
        assertFalse(result2.contains(orders.get(0)));
    }

    @Test
    void oneInProgressOrderStored_deleteRequest_ReturnsNOT_ACCEPTABLE() {
        Order order1 = Objects.requireNonNull(orderController.add(orderDto2).getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(entityUrl + "/" + order1.getId(), HttpMethod.DELETE, httpEntity, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("orderId", order1.getId().toString());
            put("status", order1.getStatus().toString());
            put("message", "Order '1' is IN_PROGRESS, and can't be deleted!");
        }};
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void emptyDatabase_invalidDeleteRequest_ReturnsBAD_REQUEST() {
        Long invalidId = -200L;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(entityUrl + "/" + invalidId, HttpMethod.DELETE, httpEntity, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put("-200", "Needs to be greater or equal to 1!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void emptyDatabase_deleteNonExistentOrder_ReturnsNOT_FOUND() {
        Long wrongId = 3000L;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(entityUrl + "/" + wrongId, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Record not found!", response.getBody());
    }

    @Test
    void someOrderStored_getByProductNameLike_returnsOrdersWithProductNameLikeGivenString() {
        String searchWord = "ay";
        List<OrderDto> testData = new ArrayList<>(List.of(orderDto1, orderDto2, orderDto3));
        List<Order> orders = testData.stream().map(o -> orderController.add(o).getBody()).toList();
        Set<Long> orderIdsWithNameLike = orders.stream()
                .filter(o -> o.getProduct().getName().contains(searchWord))
                .map(Order::getId)
                .collect(Collectors.toSet());
        ResponseEntity<List<Order>> response = orderController.getByProductNameLike(searchWord);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Long> resultOrderIds = Objects.requireNonNull(response.getBody()).stream().map(Order::getId).toList();
        assertEquals(orderIdsWithNameLike.size(), resultOrderIds.size());
        assertTrue(resultOrderIds.containsAll(orderIdsWithNameLike));
    }

    @Test
    void emptyDatabase_getByProductNameLikeInvalidRequest_returnsBAD_REQUEST() {
        String searchWord = "1".repeat(41);
        ResponseEntity<Object> response = restTemplate.getForEntity(entityUrl + "?nameLike=" + searchWord, Object.class);
        Map<String, String> expectedBody = new HashMap<>() {{
            put(searchWord, "Max 40 characters!");
        }};
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, Objects.requireNonNull(response.getBody()));
    }
}
