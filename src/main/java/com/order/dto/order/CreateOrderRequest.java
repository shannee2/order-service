//package com.order.dto.order;
//
//import java.util.List;
//
//
//public class CreateOrderRequest {
//    private Long userId;
//    private Long restaurantId;
//    private double grandTotal;
//    private String currency;
//    private List<OrderItemRequest> items;
//    private List<OrderChargeRequest> charges;
//
//    public CreateOrderRequest() {}
//
//    public CreateOrderRequest(Long userId, Long restaurantId, double grandTotal, String currency, List<OrderItemRequest> items, List<OrderChargeRequest> charges) {
//        this.userId = userId;
//        this.restaurantId = restaurantId;
//        this.grandTotal = grandTotal;
//        this.currency = currency;
//        this.items = items;
//        this.charges = charges;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public Long getRestaurantId() {
//        return restaurantId;
//    }
//
//    public double getGrandTotal() {
//        return grandTotal;
//    }
//
//    public String getCurrency() {
//        return currency;
//    }
//
//    public List<OrderItemRequest> getItems() {
//        return items;
//    }
//
//    public List<OrderChargeRequest> getCharges() {
//        return charges;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//
//    public void setRestaurantId(Long restaurantId) {
//        this.restaurantId = restaurantId;
//    }
//
//    public void setGrandTotal(double grandTotal) {
//        this.grandTotal = grandTotal;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
//
//    public void setItems(List<OrderItemRequest> items) {
//        this.items = items;
//    }
//
//    public void setCharges(List<OrderChargeRequest> charges) {
//        this.charges = charges;
//    }
//}
