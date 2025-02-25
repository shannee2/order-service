package com.order.model.order;

import com.order.model.enums.OrderStatus;
import com.order.model.money.Money;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderHistory> orderHistories = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderCharge> orderCharges = new ArrayList<>();

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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public List<OrderHistory> getOrderHistories() {
        return orderHistories;
    }

    public List<OrderCharge> getOrderCharges() {
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

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderHistories(List<OrderHistory> orderHistories) {
        this.orderHistories = orderHistories;
    }

    public void setOrderCharges(List<OrderCharge> orderCharges) {
        this.orderCharges = orderCharges;
    }
}
