package com.orderandwarehouse.app.model;

import com.orderandwarehouse.app.converter.DimensionsConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {

    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name="[name]")
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    private String name;
    @Size(max = 10, message = MAX_SIZE_MESSAGE)
    private String version;
    @NotNull
    @OneToOne
    private PartsList partsList;
    @Convert(converter = DimensionsConverter.class)
    @Size(max = 8, message = MAX_SIZE_MESSAGE)
    private Dimensions dimensions; //todo replace with String llxWWxhh (cm) + validate
    private Integer weightInGrammes;
    @Column(name="[visible]")
    private boolean visible = true;
    @OneToMany(mappedBy = "product")
    private List<Order> orders;
}
