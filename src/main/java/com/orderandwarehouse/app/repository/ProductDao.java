package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findAllByVisibleTrueOrderByNameAscVersionAscIdAsc();

    Optional<Product> findByIdAndVisibleTrue(Long id);
}
