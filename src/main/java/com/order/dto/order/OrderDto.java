package com.order.dto.order;

import com.order.dto.restaurant.RestaurantResponse;
import com.order.model.enums.OrderStatus;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderCharge;
import com.order.model.order.OrderItem;

import java.util.HashSet;
import java.util.Set;

public class OrderDto {
    private Long orderId;
    private OrderStatus orderStatus;
    private Money grandTotal;
    private Long createdBy;
    private RestaurantDto restaurant;
    private Set<OrderItemDto> orderItems = new HashSet<>();
    private Set<OrderChargeDto> orderCharges = new HashSet<>();

    public OrderDto(Order order, RestaurantResponse restaurantResponse) {
        this.orderId = order.getOrderId();
        this.orderStatus = order.getOrderStatus();
        this.grandTotal = order.getGrandTotal();
        this.createdBy = order.getCreatedBy();
        this.restaurant = new RestaurantDto(restaurantResponse);
        System.out.println("DTO KA restaurant "+this.restaurant);

        for(OrderItem orderItem : order.getOrderItems()){
            System.out.println("YE ITEM ID: "+orderItem.getOrderItemExternalId());
            this.orderItems.add(new OrderItemDto(orderItem));
        }
        for(OrderCharge orderCharge : order.getOrderCharges()){
            this.orderCharges.add(new OrderChargeDto(orderCharge));
        }
    }

    // Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Money getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Money grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Set<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public Set<OrderChargeDto> getOrderCharges() {
        return orderCharges;
    }

    public void setOrderCharges(Set<OrderChargeDto> orderCharges) {
        this.orderCharges = orderCharges;
    }

    public RestaurantDto getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDto restaurant) {
        this.restaurant = restaurant;
    }
}