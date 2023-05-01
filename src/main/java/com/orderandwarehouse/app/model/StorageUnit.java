package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "storage_unit")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class StorageUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private Component component;
    @Column(name = "[row]")
    private Integer row;
    @Column(name = "[column]")
    private Integer column;
    private Integer shelf;
    private Double quantity;
    @Column(name = "[full]")
    private boolean full;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getComponentName() {
        if (component != null) return component.getName();
        else return null;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isEmpty() {
        return component == null && (quantity == null || quantity == 0);
    }
}
