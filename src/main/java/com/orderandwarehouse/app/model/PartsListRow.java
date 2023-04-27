package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "parts_list_row")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PartsListRow {

    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    private Product product;
    @NotNull
    @ManyToOne
    private Component component;
    @NotNull
    private Double quantity;
    @Size(max = 4, message = MAX_SIZE_MESSAGE)
    private String unit = "pcs";
    @Column(name="[comment]")
    @Size(max = 1000, message = MAX_SIZE_MESSAGE)
    private String Comment;
    @NotNull
    private LocalDateTime dateAdded;
    @NotNull
    private LocalDateTime dateModified;
}
