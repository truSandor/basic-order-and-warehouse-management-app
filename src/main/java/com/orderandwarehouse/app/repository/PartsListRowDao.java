package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.PartsListRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PartsListRowDao extends JpaRepository<PartsListRow, Long> {
    List<PartsListRow> findAllByProductId(Long productId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from PartsListRow plr where plr.product.id = :product_id")
    void deleteByProduct_Id(@Param("product_id") Long productId);
}
