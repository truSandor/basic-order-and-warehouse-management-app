package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findAllByVisibleTrueOrderByNameAscVersionAscIdAsc();

    Optional<Product> findByIdAndVisibleTrue(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Product p set p.visible = false where p.id = :id")
    Integer setInvisibleById(@Param("id") Long id);
}
