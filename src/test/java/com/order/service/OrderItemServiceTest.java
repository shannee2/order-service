package com.order.service;

import com.order.grpc.OrderItemRequest;
import com.order.grpc.OrderItemResponse;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderItem;
import com.order.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
    }

    @Test
    void shouldMapOrderItemsCorrectly() {
        OrderItem item1 = new OrderItem();
        item1.setOrderItemExternalId(1L);
        item1.setQuantity(2);
        item1.setUnitPrice(new Money(10.5));

        OrderItem item2 = new OrderItem();
        item2.setOrderItemExternalId(2L);
        item2.setQuantity(1);
        item2.setUnitPrice(new Money(20.0));

        Set<OrderItem> orderItems = Set.of(item1, item2);

        List<OrderItemResponse> response = orderItemService.mapOrderItems(orderItems);

        for(OrderItemResponse item : response) {
            if(item.getItemId() == 1L) {
                assertThat(item.getQuantity()).isEqualTo(2);
                assertThat(item.getUnitPrice()).isEqualTo(10.5);
            } else if(item.getItemId() == 2L) {
                assertThat(item.getQuantity()).isEqualTo(1);
                assertThat(item.getUnitPrice()).isEqualTo(20.0);
            }
        }
    }

    @Test
    void shouldCreateOrderItemsCorrectly() {
        OrderItemRequest request1 = OrderItemRequest.newBuilder()
                .setItemId(1L)
                .setQuantity(2)
                .setUnitPrice(10.5)
                .build();

        OrderItemRequest request2 = OrderItemRequest.newBuilder()
                .setItemId(2L)
                .setQuantity(1)
                .setUnitPrice(20.0)
                .build();

        List<OrderItemRequest> requests = List.of(request1, request2);

        List<OrderItem> createdItems = orderItemService.createOrderItems(requests, order);

        assertThat(createdItems).hasSize(2);
        assertThat(createdItems.get(0).getOrderItemExternalId()).isEqualTo(1L);
        assertThat(createdItems.get(0).getQuantity()).isEqualTo(2);
        assertThat(createdItems.get(0).getUnitPrice()).isEqualTo(10.5);

        verify(orderItemRepository, times(1)).saveAll(createdItems);
    }
}
