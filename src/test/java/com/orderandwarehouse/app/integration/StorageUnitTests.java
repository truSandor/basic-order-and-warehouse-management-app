package com.orderandwarehouse.app.integration;


import com.orderandwarehouse.app.model.Type;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.model.dto.StorageUnitDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StorageUnitTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;
    private String entityUrl;
    private String baseUrl;


    ComponentDto componentDto1;
    ComponentDto componentDto2;
    ComponentDto componentDto3;

    StorageUnitDto storageUnitDto1;
    StorageUnitDto storageUnitDto2;
    StorageUnitDto storageUnitDto3;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        entityUrl = baseUrl + "/components";
        componentDto1 = ComponentDto.builder().name("component1").type(Type.SMD).build();
        componentDto2 = ComponentDto.builder().name("component2").type(Type.SMD).build();
        componentDto3 = ComponentDto.builder().name("component3").type(Type.THD).build();

        storageUnitDto1 = StorageUnitDto.builder().row(1).column(1).shelf(1).build();
        storageUnitDto2 = StorageUnitDto.builder().row(1).column(1).shelf(2).build();
        storageUnitDto3 = StorageUnitDto.builder().row(1).column(1).shelf(3).build();
    }

}
