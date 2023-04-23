package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageUnitDao extends JpaRepository<StorageUnit, Long> {
    List<StorageUnit> findAllByVisibleTrueOrderByIdAsc();


    Optional<StorageUnit> findByIdAndVisibleTrue(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update StorageUnit su set su.visible = false where su.id = :id")
    Integer setInvisibleById(@Param("id") Long id);

    List<StorageUnit> findAllByVisibleTrueAndComponent_Id(Long ComponentId);
}
