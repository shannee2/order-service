package com.order.service;

import com.order.grpc.OrderItemRequest;
import com.order.grpc.OrderItemResponse;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderItem;
import com.order.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItemResponse> mapOrderItems(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> OrderItemResponse.newBuilder()
                        .setItemId(item.getOrderItemExternalId())
                        .setQuantity(item.getQuantity())
                        .setUnitPrice(item.getUnitPrice())
                        .build())
                .toList();
    }

    public List<OrderItem> createOrderItems(List<OrderItemRequest> orderItemRequests, Order order){
        List<OrderItem> items = orderItemRequests.stream().map(orderItemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setOrderItemExternalId(orderItemRequest.getItemId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setUnitPrice(new Money(orderItemRequest.getUnitPrice()));
            return orderItem;
        }).toList();
        orderItemRepository.saveAll(items);
        return items;
    }
}
