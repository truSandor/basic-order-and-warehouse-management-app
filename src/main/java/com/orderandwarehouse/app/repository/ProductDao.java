package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findAllByIdNotNullOrderByNameAscVersionAsc();

}
