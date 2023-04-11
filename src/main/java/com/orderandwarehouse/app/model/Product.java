package com.orderandwarehouse.app.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nonnull
    private String name;
    private String version;
    @OneToOne
    private PartsList partsList;
    @Transient
    private Dimensions dimensions;
    private Integer weightInGrammes;
    private boolean isVisible = true;

    @Column(name = "dimensions")
    protected String getDimensionsAsString() {
        return dimensions != null ? dimensions.toString() : null;
    }

    protected void setDimensionsAsString(String dimensions){
        this.dimensions = new Dimensions(dimensions);
    }

}
