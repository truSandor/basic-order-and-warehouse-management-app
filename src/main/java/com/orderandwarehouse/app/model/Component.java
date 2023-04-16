package com.orderandwarehouse.app.model;

import com.orderandwarehouse.app.converter.DimensionsConverter;
import jakarta.annotation.Nonnull;
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
    @Column(length = 120)
    @Nonnull
    private String name;
    @Nonnull
    private Type type;
    private Double primaryValue;
    @Column(length = 4)
    private String primaryUnit;
    private Double secondaryValue;
    @Column(length = 4)
    private String secondaryUnit;
    private Integer tolerance;
    @Convert(converter = DimensionsConverter.class)
    @Column(length = 11)
    private Dimensions packageDimensions;
    private Double weightInGrammes;
    @Column(length = 40)
    private String manufacturerId;
    @Column(length = 40)
    private String traderComponentId;
    @OneToMany(mappedBy = "component")
    private List<StorageUnit> storageUnits;
    @OneToMany(mappedBy = "component")
    private List<PartsListRow> partsListRows;
    private boolean visible = true;
}
