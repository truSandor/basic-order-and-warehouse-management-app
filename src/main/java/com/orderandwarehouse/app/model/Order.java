package com.orderandwarehouse.app.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "[order]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nonnull
    @ManyToOne
    private Product product;
    @Nonnull
    private Long quantity;
    @Nonnull
    private LocalDateTime dateReceived;
    private LocalDateTime deadline;
    private LocalDateTime dateStarted;
    private LocalDateTime dateCompleted;
    private Status status = Status.NOT_STARTED;
    @Column(name="[visible]")
    private boolean visible = true;
}
