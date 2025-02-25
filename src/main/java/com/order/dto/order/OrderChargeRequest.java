package com.order.dto.order;

import com.order.model.enums.ChargeType;


public class OrderChargeRequest {
    private ChargeType chargeType;
    private double chargeCost;

    public OrderChargeRequest() {}

    public OrderChargeRequest(ChargeType chargeType, double chargeCost) {
        this.chargeType = chargeType;
        this.chargeCost = chargeCost;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public double getChargeCost() {
        return chargeCost;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public void setChargeCost(double chargeCost) {
        this.chargeCost = chargeCost;
    }
}
