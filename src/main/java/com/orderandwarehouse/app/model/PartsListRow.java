package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "parts_list_row")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PartsListRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Component component;
    private Double quantity;
    private String unit = "pcs";
    @Column(name = "[comment]")
    private String Comment;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getComponentName(){
        return component.getName();
    }
}
