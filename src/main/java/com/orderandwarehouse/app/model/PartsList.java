package com.orderandwarehouse.app.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private LocalDateTime date_added;
    @NotNull
    private LocalDateTime date_modified;
    @Column(name="[visible]")
    private boolean visible = true;
}
