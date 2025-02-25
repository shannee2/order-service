package com.order.model.order;

import com.order.model.enums.OrderStatus;
import com.order.model.money.Money;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;
import java.util.Set;

@Entity
@Table(name = "order_header")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private Money grandTotal;

    private Long createdBy; // User ID

    private Long restaurantId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    private Set<OrderHistory> orderHistories = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    private Set<OrderCharge> orderCharges = new HashSet<>();

    public void addHistory(OrderHistory orderHistory) {
        orderHistories.add(orderHistory);
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getGrandTotal() {
        return grandTotal;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Set<OrderHistory> getOrderHistories() {
        return orderHistories;
    }

    public Set<OrderCharge> getOrderCharges() {
        return orderCharges;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setGrandTotal(Money grandTotal) {
        this.grandTotal = grandTotal;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderHistories(Set<OrderHistory> orderHistories) {
        this.orderHistories = orderHistories;
    }

    public void setOrderCharges(Set<OrderCharge> orderCharges) {
        this.orderCharges = orderCharges;
    }
}
