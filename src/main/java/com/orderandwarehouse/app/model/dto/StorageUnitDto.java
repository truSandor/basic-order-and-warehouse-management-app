package com.orderandwarehouse.app.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageUnitDto {
    private Long componentId;
    private Double quantity;
    private boolean full;
}
