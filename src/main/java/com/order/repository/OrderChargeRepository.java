package com.order.repository;

import com.order.model.order.OrderCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderChargeRepository extends JpaRepository<OrderCharge, Long> {
}
