package com.orderandwarehouse.app.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "parts_list_row")
public class PartsListRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nonnull
    @ManyToOne
    private PartsList partsList;
    @Nonnull
    @ManyToOne
    private Component component;
    @Nonnull
    private Double quantity;
    @Column(length = 4)
    private String unit = "pc";
    @Column(length = 1000)
    private String Comment;
    @Nonnull
    private LocalDateTime dateAdded;
    @Nonnull
    private LocalDateTime dateModified;
    private boolean visible = true;
}
