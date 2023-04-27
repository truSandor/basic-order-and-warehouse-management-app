package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
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
    @JsonIdentityReference(alwaysAsId = true)
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
}
