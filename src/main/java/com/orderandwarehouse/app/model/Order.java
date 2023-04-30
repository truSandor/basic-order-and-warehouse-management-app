package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "[order]")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;
    @NotNull
    @Min(value = 1)
    private Integer quantity;
    @NotNull
    private LocalDateTime dateReceived;
    private LocalDateTime deadline;
    private LocalDateTime dateStarted;
    private LocalDateTime dateCompleted;
    @NotNull
    private LocalDateTime dateAdded;
    @NotNull
    private LocalDateTime dateModified;
    @NotNull
    private Status status;

    @JsonIgnore
    public boolean isActive() {
        return status.equals(Status.IN_PROGRESS) || status.equals(Status.OVERDUE);
    }
}
