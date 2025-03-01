package com.order.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryAssignmentDto {
    @JsonProperty("order_id")  // Ensures correct mapping
    private Long orderId;

    @JsonProperty("delivery_partner_id")
    private Long deliveryPartnerId;

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(Long deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    @Override
    public String toString() {
        return "DeliveryAssignmentDTO{" +
                "orderId=" + orderId +
                ", deliveryPartnerId=" + deliveryPartnerId +
                '}';
    }
}
