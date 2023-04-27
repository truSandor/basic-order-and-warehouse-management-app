package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findAllByIdNotNullOrderByDateReceived();

    List<Order> findAllByProduct_NameIsContainingIgnoreCase(String nameLike);

}
