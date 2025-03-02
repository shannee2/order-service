package com.order.service;

import com.order.model.enums.OrderStatus;
import com.order.model.order.Order;
import com.order.model.order.OrderHistory;
import com.order.repository.OrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    @Autowired
    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public OrderHistory createOrderHistory(Order order, OrderStatus orderStatus){
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatusId(orderStatus);
        orderHistory.setStatusDate(java.time.LocalDateTime.now());
        orderHistoryRepository.save(orderHistory);
        return orderHistory;
    }
}
