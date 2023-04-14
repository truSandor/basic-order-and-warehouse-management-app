package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.PartsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartsListDao extends JpaRepository<PartsList, Long> {
}
