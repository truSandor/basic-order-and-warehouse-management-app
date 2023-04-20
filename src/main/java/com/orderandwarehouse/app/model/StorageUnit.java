package com.orderandwarehouse.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Column(name = "[row]")
    private Integer row;
    @NotNull
    @Column(name = "[column]")
    private Integer column;
    @NotNull
    private Integer shelf;
    private Double quantity;
    @Column(name = "[full]")
    private boolean full;
    @Column(name="[visible]")
    private boolean visible = true;
}
