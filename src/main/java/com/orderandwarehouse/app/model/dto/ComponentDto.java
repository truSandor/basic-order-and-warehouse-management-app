package com.orderandwarehouse.app.model.dto;

import com.orderandwarehouse.app.model.Type;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComponentDto {
    //todo check if adding ID throws exception when adding new component
    private String name;
    private Type type;
    private Double primaryValue;
    private String primaryUnit;
    private Double secondaryValue;
    private String secondaryUnit;
    private Integer tolerance;
    private String packageDimensions;
    private Double weightInGrammes;
    private String manufacturerId;
    private String traderComponentId;
    private List<Long> storageUnitIds;
    private List<Long> partsListRowsIds;
}
