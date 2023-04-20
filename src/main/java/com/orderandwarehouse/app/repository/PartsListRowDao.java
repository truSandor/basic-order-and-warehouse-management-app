package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.PartsListRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartsListRowDao extends JpaRepository<PartsListRow, Long> {
    List<PartsListRow> findAllByVisibleTrueAndAndPartsListIdOrderById(Long partsListId);

    @Modifying(clearAutomatically = true)
    @Query("update PartsListRow plr set plr.visible = false where plr.component.id = :componentId")
    void setInvisibleByComponentId(Long componentId);

    List<PartsListRow> findAllByVisibleTrueAndComponent_Id(Long componentId);
}
