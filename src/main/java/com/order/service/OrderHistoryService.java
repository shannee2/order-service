package com.order.service;

import com.order.model.enums.OrderStatus;
import com.order.model.order.Order;
import com.order.model.order.OrderHistory;
import org.springframework.stereotype.Service;

@Service
public class OrderHistoryService {
    public OrderHistory createOrderHistory(Order order, OrderStatus orderStatus){
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatusId(orderStatus);
        orderHistory.setStatusDate(java.time.LocalDateTime.now());
        return orderHistory;
    }
}
