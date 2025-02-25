//package com.order.dto.order;
//
//import com.order.model.order.Order;
//
//
//import java.util.List;
//
//
//public class CreateOrderResponse {
//    private Long orderId;
//    private RestaurantInfo restaurant;
//    private List<OrderItemInfo> items;
//
//    public CreateOrderResponse(RestaurantInfo restaurantInfo, List<OrderItemInfo> orderItemInfos) {
//        this.restaurant = restaurantInfo;
//        this.items = orderItemInfos;
//    }
//
//    public Long getOrderId() {
//        return orderId;
//    }
//
//    public RestaurantInfo getRestaurant() {
//        return restaurant;
//    }
//
//    public List<OrderItemInfo> getItems() {
//        return items;
//    }
//
//    public void setOrderId(Long orderId) {
//        this.orderId = orderId;
//    }
//
//    public void setRestaurant(RestaurantInfo restaurant) {
//        this.restaurant = restaurant;
//    }
//
//    public void setItems(List<OrderItemInfo> items) {
//        this.items = items;
//    }
//
////    public CreateOrderResponse(Order order) {
////        this.orderId = order.getOrderId();
////        this.restaurant = new RestaurantInfo(order.getRestaurantId(), );
////    }
//
//    public static class RestaurantInfo {
//        private Long restaurantId;
//        private String restaurantName;
//
//        public RestaurantInfo(Long restaurantId, String restaurantName) {
//            this.restaurantId = restaurantId;
//            this.restaurantName = restaurantName;
//        }
//
//        public Long getRestaurantId() {
//            return restaurantId;
//        }
//
//        public String getRestaurantName() {
//            return restaurantName;
//        }
//
//        public void setRestaurantId(Long restaurantId) {
//            this.restaurantId = restaurantId;
//        }
//
//        public void setRestaurantName(String restaurantName) {
//            this.restaurantName = restaurantName;
//        }
//    }
//
//    public static class OrderItemInfo {
//        private Long orderItemId;
//        private String itemName;
//
//        public OrderItemInfo(Long orderItemId, String itemName) {
//            this.orderItemId = orderItemId;
//            this.itemName = itemName;
//        }
//
//        public Long getOrderItemId() {
//            return orderItemId;
//        }
//
//        public String getItemName() {
//            return itemName;
//        }
//
//        public void setOrderItemId(Long orderItemId) {
//            this.orderItemId = orderItemId;
//        }
//
//        public void setItemName(String itemName) {
//            this.itemName = itemName;
//        }
//    }
//}
