package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "component")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Component {

    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "[name]")
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    @NotBlank(message = "Name must not be blank!")
    private String name;
    @NotNull
    @Column(name = "[type]")
    private Type type;
    private Double primaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String primaryUnit;
    private Double secondaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String secondaryUnit;
    private Integer tolerance;
    @Size(max = 8)
    @Pattern(regexp = "^\\d{1,2}x\\d{1,2}x\\d{1,2}$", message = "Dimensions pattern: \"LLxWWxHH\" in cm")
    private String packageDimensions;
    private Double weightInGrammes;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String manufacturerId;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String traderComponentId;
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
