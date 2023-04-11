package com.orderandwarehouse.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "parts_list")
public class PartsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "partsList")
    private List<PartsListRow> partsListRows;
    private LocalDateTime date_added;
    private LocalDateTime date_modified;
    private boolean isVisible = true;
}
