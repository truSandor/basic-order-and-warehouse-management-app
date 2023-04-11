package com.orderandwarehouse.app.model;

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
    @ManyToOne
    private PartsList partsList;
    @ManyToOne
    private Component component;
    private Double quantity;
    private String unit;
    private String Comment;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;
    private boolean isVisible;
}
