package com.orderandwarehouse.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "component")
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Type type;
    private Double primaryValue;
    private String primaryUnit;
    private Double secondaryValue;
    private String secondaryUnit;
    private Integer tolerance;
    @Transient
    private Dimensions packageDimensions;
    private Integer weightInGrammes;
    private String manufacturerId;
    private String traderComponentId;
    @OneToMany(mappedBy = "component")
    private List<StorageUnit> storageUnits;
//    @OneToMany(mappedBy = "component")
//    private List<PartsListRow> partsListRows;
    private boolean isVisible = true;

    @Column(name = "packageDimensions")
    protected String getDimensionsAsString() {
        return packageDimensions != null ? packageDimensions.toString() : null;
    }

    protected void setDimensionsAsString(String packageDimensions){
        this.packageDimensions = new Dimensions(packageDimensions);
    }
}
