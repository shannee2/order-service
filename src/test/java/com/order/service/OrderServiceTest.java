package com.order.service;

import com.order.grpc.*;
import com.order.model.enums.ChargeType;
import com.order.model.enums.OrderStatus;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderCharge;
import com.order.model.order.OrderHistory;
import com.order.model.order.OrderItem;
import com.order.repository.OrderChargeRepository;
import com.order.repository.OrderHistoryRepository;
import com.order.repository.OrderItemRepository;
import com.order.repository.OrderRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private OrderChargeRepository orderChargeRepository;
    @Mock private OrderHistoryRepository orderHistoryRepository;
    @Mock private RestTemplate restTemplate;
    @Mock private AuthService authService;
    @Mock private RestaurantService restaurantService;

    @InjectMocks private OrderService orderService;

    private Order order;
    private Set<OrderItem> orderItems;
    private Set<OrderCharge> orderCharges;
    private OrderHistory orderHistory;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setGrandTotal(new Money(100.0, "USD"));
        order.setCreatedBy(101L);
        order.setRestaurantId(202L);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemExternalId(1L);
        orderItem.setQuantity(2);
        orderItem.setUnitPrice(new Money(50.0));

        OrderCharge orderCharge = new OrderCharge();
        orderCharge.setChargeType(ChargeType.DELIVERY_FEES);
        orderCharge.setChargeCost(new Money(10.0));

        orderItems = new HashSet<>(List.of(orderItem));
        orderCharges = new HashSet<>(List.of(orderCharge));

        order.setOrderItems(orderItems);
        order.setOrderCharges(orderCharges);

        orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatusId(OrderStatus.ORDER_CREATED);
    }

    @Test
    void createOrder_ShouldSaveOrderAndReturnOrderId() {
        // Mock request
        CreateOrderRequest request = CreateOrderRequest.newBuilder()
                .setGrandTotal(100.0)
                .setCurrency("USD")
                .setUserId(101L)
                .setRestaurantId(202L)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.saveAll(anySet())).thenReturn(new ArrayList<>(orderItems));
        when(orderChargeRepository.saveAll(anySet())).thenReturn(new ArrayList<>(orderCharges));
        when(orderHistoryRepository.save(any(OrderHistory.class))).thenReturn(orderHistory);

        // Execute
        Long orderId = orderService.createOrder(request);

        // Verify
        assertNotNull(orderId);
        assertEquals(1L, orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anySet());
        verify(orderChargeRepository, times(1)).saveAll(anySet());
        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
    }

    @Test
    void getOrderDetails_ShouldReturnOrderResponse_WhenOrderExists() {
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.of(order));

        // Execute
        GetOrderResponse response = orderService.getOrderDetails(1L);

        // Verify
        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("ORDER_CREATED", response.getOrderStatus());
        assertEquals(100.0, response.getGrandTotal());
        assertEquals("USD", response.getCurrency());
        assertEquals(101L, response.getCreatedBy());
        assertEquals(202L, response.getRestaurantId());
        assertEquals(1, response.getItemsCount());
        assertEquals(1, response.getChargesCount());
    }

    @Test
    void getOrderDetails_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.getOrderDetails(1L));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findByOrderId(1L);
    }
}
