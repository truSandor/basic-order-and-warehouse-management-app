package com.orderandwarehouse.app.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "storage_unit")
public class StorageUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Component component;
    @Nonnull
    @Column(name = "[row]")
    private Integer row;
    @Nonnull
    @Column(name = "[column]")
    private Integer column;
    @Nonnull
    @Column(name = "shelf")
    private Integer shelf;
    private Double quantity;
    private boolean full;
    private boolean visible = true;
}
