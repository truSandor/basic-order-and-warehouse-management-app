package com.orderandwarehouse.app.data;

import com.orderandwarehouse.app.model.Type;
import com.orderandwarehouse.app.model.dto.ComponentDto;

public interface TestComponentDto {
    ComponentDto component1 = ComponentDto.builder()
            .name("component1")
            .type(Type.SMD)
            .build();
}
