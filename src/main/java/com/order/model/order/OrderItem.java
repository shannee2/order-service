package com.order.model.order;

import com.order.model.money.Money;
import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Long orderItemExternalId;

    private Double unitPrice;

    private int quantity;

    public OrderItem() {

    }

    public void setUnitPrice(Money money){
        this.unitPrice = money.getAmount();
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderItemExternalId() {
        return orderItemExternalId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setOrderItemExternalId(Long orderItemExternalId) {
        this.orderItemExternalId = orderItemExternalId;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
