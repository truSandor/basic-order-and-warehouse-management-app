package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.PartsListRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartsListRowDao extends JpaRepository<PartsListRow, Long> {
    List<PartsListRow> findAllByVisibleTrueAndAndPartsListIdOrderById(Long partsListId);
}
