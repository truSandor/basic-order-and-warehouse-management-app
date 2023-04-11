package com.orderandwarehouse.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
//@Entity
//@Table(name = "BOM")
public class BillOfMaterials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//TODO find out how to use it, then implement it
}
