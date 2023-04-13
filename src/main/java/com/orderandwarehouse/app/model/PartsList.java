package com.orderandwarehouse.app.model;

import jakarta.annotation.Nonnull;
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
    @Nonnull
    private LocalDateTime date_added;
    @Nonnull
    private LocalDateTime date_modified;
    private boolean visible = true;
}
