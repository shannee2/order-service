package com.order.dto.order;

import com.order.model.money.Money;
import com.order.model.order.OrderItem;

public class OrderItemDto {
    private Long orderItemId;
    private int quantity;
    private Double unitPrice;

    public OrderItemDto(OrderItem orderItem) {
        this.orderItemId = orderItem.getOrderItemExternalId();
        this.quantity = orderItem.getQuantity();
        this.unitPrice = orderItem.getUnitPrice();
    }

    // Getters and Setters

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}