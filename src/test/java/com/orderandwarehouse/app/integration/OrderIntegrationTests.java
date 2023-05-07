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

        ProductDto productDto1 = ProductDto.builder().name("product1").build();
        ProductDto productDto2 = ProductDto.builder().name("product2").build();
        ProductDto productDto3 = ProductDto.builder().name("product3").build();
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
    void emptyDataBase_getByInValidId_ReturnsBAD_REQUEST() {
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
}
