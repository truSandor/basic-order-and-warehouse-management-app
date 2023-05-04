package com.orderandwarehouse.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "[name]")
    private String name;
    private String version;
    private String dimensions;
    private Integer weightInGrammes;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    private List<PartsListRow> partsList;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    private List<Order> orders;

    @JsonIgnore
    public boolean isInUse() {
        return hasPartsList() || hasActiveOrders();
    }

    @JsonIgnore
    public boolean hasActiveOrders() {
        return orders != null && orders.stream().anyMatch(Order::isActive);
    }

    @JsonIgnore
    public List<Long> getActiveOrderIds() {
        if (orders == null) return List.of();
        return orders.stream()
                .filter(Order::isActive)
                .map(Order::getId)
                .toList();
    }

    @JsonIgnore
    public boolean hasPartsList() {
        return partsList != null && !partsList.isEmpty();
    }
}
