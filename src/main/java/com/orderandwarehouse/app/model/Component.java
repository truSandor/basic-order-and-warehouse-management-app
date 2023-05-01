package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "component")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "[name]")
    private String name;
    @Column(name = "[type]")
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
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;
    @OneToMany(mappedBy = "component")
    @JsonIdentityReference(alwaysAsId = true)
    private List<StorageUnit> storageUnits;
    @OneToMany(mappedBy = "component")
    @JsonIdentityReference(alwaysAsId = true)
    private List<PartsListRow> partsListRows;

    @JsonIgnore
    public List<Long> getStorageUnitIds() {
        if (storageUnits == null) return List.of();
        return storageUnits.stream().map(StorageUnit::getId).toList();
    }

    @JsonIgnore
    public List<Long> getProductIds() {
        if (partsListRows == null) return List.of();
        return partsListRows.stream().map(plr -> plr.getProduct().getId()).toList();
    }

    @JsonIgnore
    public boolean isInUse() {
        return (storageUnits != null && !storageUnits.isEmpty()) ||
                (partsListRows != null && !partsListRows.isEmpty());
    }
}
