package com.order.service;

import com.order.model.enums.OrderStatus;
import com.order.model.order.Order;
import com.order.model.order.OrderHistory;
import com.order.repository.OrderHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @InjectMocks
    private OrderHistoryService orderHistoryService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
    }

    @Test
    void shouldCreateOrderHistoryCorrectly() {
        OrderStatus status = OrderStatus.ORDER_CREATED;
        OrderHistory savedHistory = new OrderHistory();
        savedHistory.setOrder(order);
        savedHistory.setStatusId(status);
        savedHistory.setStatusDate(LocalDateTime.now());

        when(orderHistoryRepository.save(any(OrderHistory.class))).thenReturn(savedHistory);

        OrderHistory result = orderHistoryService.createOrderHistory(order, status);

        assertThat(result.getOrder()).isEqualTo(order);
        assertThat(result.getStatusId()).isEqualTo(status);
        assertThat(result.getStatusDate()).isNotNull();

        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
    }
}
