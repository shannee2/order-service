package com.order.service;

import com.order.grpc.OrderChargeRequest;
import com.order.grpc.OrderChargeResponse;
import com.order.model.enums.ChargeType;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderCharge;
import com.order.repository.OrderChargeRepository;
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
class OrderChargeServiceTest {

    @Mock
    private OrderChargeRepository orderChargeRepository;

    @InjectMocks
    private OrderChargeService orderChargeService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
    }

    @Test
    void shouldMapOrderChargesCorrectly() {
        OrderCharge charge1 = new OrderCharge();
        charge1.setChargeType(ChargeType.DELIVERY_FEES);
        charge1.setChargeCost(new Money(5.0));

        OrderCharge charge2 = new OrderCharge();
        charge2.setChargeType(ChargeType.GST);
        charge2.setChargeCost(new Money(2.5));

        Set<OrderCharge> orderCharges = Set.of(charge1, charge2);

        List<OrderChargeResponse> response = orderChargeService.mapOrderCharges(orderCharges);

        for (OrderChargeResponse charge : response) {
            if (charge.getChargeType().equals("DELIVERY_FEES")) {
                assertThat(charge.getChargeCost()).isEqualTo(5.0);
            } else if (charge.getChargeType().equals("GST")) {
                assertThat(charge.getChargeCost()).isEqualTo(2.5);
            }
        }
    }

    @Test
    void shouldCreateOrderChargesCorrectly() {
        OrderChargeRequest request1 = OrderChargeRequest.newBuilder()
                .setChargeType("DELIVERY_FEES")
                .setChargeCost(5.0)
                .build();

        OrderChargeRequest request2 = OrderChargeRequest.newBuilder()
                .setChargeType("GST")
                .setChargeCost(2.5)
                .build();

        List<OrderChargeRequest> requests = List.of(request1, request2);

        List<OrderCharge> createdCharges = orderChargeService.createOrderCharges(requests, order);

        assertThat(createdCharges).hasSize(2);
        assertThat(createdCharges.get(0).getChargeType()).isEqualTo(ChargeType.DELIVERY_FEES);
        assertThat(createdCharges.get(0).getChargeCost()).isEqualTo(5.0);
        assertThat(createdCharges.get(1).getChargeType()).isEqualTo(ChargeType.GST);
        assertThat(createdCharges.get(1).getChargeCost()).isEqualTo(2.5);

        verify(orderChargeRepository, times(1)).saveAll(createdCharges);
    }
}
