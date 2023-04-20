package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComponentDao extends JpaRepository<Component, Long> {
    List<Component> findAllByVisibleTrueOrderByIdAsc();

    Optional<Component> findByIdAndVisibleTrue(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Component c set c.visible = false where c.id = :id")
    Integer setInvisibleById(@Param("id") Long id);

    List<Component> findAllByNameIsContainingIgnoreCaseAndVisibleTrue(String nameLike);
}
