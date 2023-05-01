package com.orderandwarehouse.app.data;

import com.orderandwarehouse.app.model.Type;
import com.orderandwarehouse.app.model.dto.ComponentDto;

public interface TestComponentDto {
    ComponentDto invalidComponentDto1 = ComponentDto.builder().name("").type(Type.SMD).build();
    ComponentDto invalidComponentDto2 = ComponentDto.builder().name("1".repeat(121)).type(Type.SMD).build();
    ComponentDto componentDto1 = ComponentDto.builder().name("component1").type(Type.SMD).build();
    ComponentDto componentDto2 = ComponentDto.builder().name("component2").type(Type.SMD).build();
    ComponentDto componentDto3 = ComponentDto.builder().name("component3").type(Type.THD).build();
}
