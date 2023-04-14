package com.orderandwarehouse.app.repository;

import com.orderandwarehouse.app.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentDao extends JpaRepository<Component, Long> {
}
