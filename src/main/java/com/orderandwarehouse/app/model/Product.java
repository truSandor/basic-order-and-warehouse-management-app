package com.orderandwarehouse.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {

    private static final String MAX_SIZE_MESSAGE = "Max {max} characters!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name="[name]")
    @Size(max = 120, message = MAX_SIZE_MESSAGE)
    private String name;
    @Size(max = 10, message = MAX_SIZE_MESSAGE)
    private String version;
    @NotNull
    @OneToOne
    private PartsList partsList;
    @Size(max = 8)
    @Pattern(regexp ="^\\d{1,2}x\\d{1,2}x\\d{1,2}$", message = "Dimensions pattern: \"LLxWWxHH\" in cm")
    private String dimensions;
    private Integer weightInGrammes;
    @Column(name="[visible]")
    private boolean visible = true;
    @OneToMany(mappedBy = "product")
    private List<Order> orders;
}
