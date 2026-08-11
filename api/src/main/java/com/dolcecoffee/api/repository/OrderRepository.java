package com.dolcecoffee.api.repository;

import com.dolcecoffee.api.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findAllByOrderByTimestampDesc();
}
