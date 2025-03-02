package com.order.service;

import com.order.dto.order.OrderDto;
import com.order.dto.restaurant.RestaurantResponse;
import com.order.grpc.CreateOrderRequest;
import com.order.grpc.GetOrderResponse;
import com.order.kafka.producer.OrderProducer;
import com.order.model.enums.OrderStatus;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderHistory;
import com.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private RestTemplate restTemplate;
    @Mock private RestaurantService restaurantService;
    @Mock private OrderItemService orderItemService;
    @Mock private OrderChargeService orderChargeService;
    @Mock private OrderHistoryService orderHistoryService;
    @Mock private OrderProducer orderProducer;

    @InjectMocks private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setGrandTotal(new Money(100.0, "USD"));
        order.setCreatedBy(101L);
        order.setRestaurantId(202L);
    }

    @Test
    void testCreateOrder() {
        CreateOrderRequest request = CreateOrderRequest.newBuilder()
                .setUserId(101L)
                .setRestaurantId(202L)
                .setGrandTotal(100.0)
                .setCurrency("USD")
                .build();

        when(restaurantService.validateRestaurantAndItemsDetails(request)).thenReturn(new RestaurantResponse());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Long orderId = orderService.createOrder(request);

        assertNotNull(orderId);
        assertEquals(1L, orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderProducer, times(1)).sendOrder(any(OrderDto.class));
    }

    @Test
    void testGetOrderDetails() {
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.of(order));

        GetOrderResponse response = orderService.getOrderDetails(1L);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("ORDER_CREATED", response.getOrderStatus());
        assertEquals(100.0, response.getGrandTotal());
        verify(orderRepository, times(1)).findByOrderId(1L);
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.findWithHistoriesByOrderId(1L)).thenReturn(Optional.of(order));
        when(orderHistoryService.createOrderHistory(any(Order.class), any(OrderStatus.class)))
                .thenReturn(new OrderHistory());

        orderService.updateOrderStatus(1L, OrderStatus.ORDER_DELIVERED);

        assertEquals(OrderStatus.ORDER_DELIVERED, order.getOrderStatus());
        verify(orderRepository, times(1)).findWithHistoriesByOrderId(1L);
    }
}
