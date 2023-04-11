package com.orderandwarehouse.app.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
public class StorageUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Component component;
    @Nonnull
    private Integer row;
    @Nonnull
    private Integer column;
    @Nonnull
    private Integer shelf;
    private Double quantity;
    private boolean isFull;
    private boolean visible = true;
}
