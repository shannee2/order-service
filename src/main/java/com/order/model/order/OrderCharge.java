package com.order.model.order;

import com.order.model.enums.ChargeType;
import com.order.model.money.Money;
import jakarta.persistence.*;

@Entity
public class OrderCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    private Double chargeCost;

    public OrderCharge(Order order, ChargeType chargeType, Money chargeCost) {
        this.order = order;
        this.chargeType = chargeType;
        this.chargeCost = chargeCost.getAmount();
    }

    public OrderCharge() {

    }

    public void setChargeCost(Money money){
        this.chargeCost = money.getAmount();
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public Double getChargeCost() {
        return chargeCost;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public void setChargeCost(Double chargeCost) {
        this.chargeCost = chargeCost;
    }
}

