package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orderandwarehouse.app.converter.DimensionsConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "component")
public class Component {

    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="[name]")
    @Size(max=120, message = MAX_SIZE_MESSAGE)
    @NotBlank(message = "Name must not be blank!")
    private String name;
    @NotNull
    @Column(name="[type]")
    private Type type;
    private Double primaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String primaryUnit;
    private Double secondaryValue;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String secondaryUnit;
    private Integer tolerance;
    @Convert(converter = DimensionsConverter.class)
    @Size(max = 8, message = MAX_SIZE_MESSAGE)
    private Dimensions packageDimensions; //todo replace with String llxWWxhh (cm) + validate
    private Double weightInGrammes;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String manufacturerId;
    @Size(max = 40, message = MAX_SIZE_MESSAGE)
    private String traderComponentId;
    @OneToMany(mappedBy = "component")
    @JsonIgnore
    private List<StorageUnit> storageUnits;
    @OneToMany(mappedBy = "component")
    @JsonIgnore
    private List<PartsListRow> partsListRows;
    @Column(name="[visible]")
    private boolean visible = true;
}
