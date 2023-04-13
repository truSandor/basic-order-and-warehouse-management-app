package com.orderandwarehouse.app.model;

import com.orderandwarehouse.app.converter.DimensionsConverter;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nonnull
    @Column(length = 120)
    private String name;
    @Column(length = 10)
    private String version;
    @Nonnull
    @OneToOne
    private PartsList partsList;
    @Convert(converter = DimensionsConverter.class)
    @Column(length = 11)
    private Dimensions dimensions;
    private Integer weightInGrammes;
    private boolean visible = true;
    @OneToMany(mappedBy = "product")
    private List<Order> orders;
}
