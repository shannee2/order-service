package com.order.dto.order;

import com.order.model.enums.ChargeType;
import com.order.model.money.Money;
import com.order.model.order.OrderCharge;

public class OrderChargeDto {
    private ChargeType chargeType;
    private Double chargeCost;


    public OrderChargeDto(OrderCharge orderCharge) {
        this.chargeType = orderCharge.getChargeType();
        this.chargeCost = orderCharge.getChargeCost();
    }

    // Getters and Setters

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public Double getChargeCost() {
        return chargeCost;
    }

    public void setChargeCost(Double chargeCost) {
        this.chargeCost = chargeCost;
    }
}