package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
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
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;
    private Integer quantity;
    private LocalDateTime dateReceived;
    private LocalDateTime deadline;
    private LocalDateTime dateStarted;
    private LocalDateTime dateCompleted;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;
    private Status status;

    @JsonIgnore
    public boolean isActive() {
        return status.equals(Status.IN_PROGRESS);
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isOverDue() {
        return (!status.equals(Status.COMPLETED) && dateCompleted == null && deadline != null && deadline.isBefore(LocalDateTime.now()));
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean completedOverDue() {
        return (status.equals(Status.COMPLETED) && dateCompleted != null && deadline != null && deadline.isBefore(dateCompleted));
    }
}
