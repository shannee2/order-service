package com.order.repository;

import com.order.model.order.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderCharges"})
    Optional<Order> findByOrderId(Long orderId);
}
