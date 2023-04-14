package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageUnitDao extends JpaRepository<StorageUnit,Long> {
}
