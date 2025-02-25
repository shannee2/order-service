package com.order.model.order;

import com.order.model.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus statusId;

    private LocalDateTime statusDate;

    public OrderHistory(Order order, OrderStatus statusId) {
        this.order = order;
        this.statusId = statusId;
        this.statusDate = LocalDateTime.now();
    }

    public OrderHistory() {

    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public OrderStatus getStatusId() {
        return statusId;
    }

    public LocalDateTime getStatusDate() {
        return statusDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setStatusId(OrderStatus statusId) {
        this.statusId = statusId;
    }

    public void setStatusDate(LocalDateTime statusDate) {
        this.statusDate = statusDate;
    }
}
