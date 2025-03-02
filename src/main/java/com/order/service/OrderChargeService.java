package com.order.service;

import com.order.grpc.OrderChargeRequest;
import com.order.grpc.OrderChargeResponse;
import com.order.model.enums.ChargeType;
import com.order.model.money.Money;
import com.order.model.order.Order;
import com.order.model.order.OrderCharge;
import com.order.repository.OrderChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderChargeService {
    private final OrderChargeRepository orderChargeRepository;

    @Autowired
    public OrderChargeService(OrderChargeRepository orderChargeRepository) {
        this.orderChargeRepository = orderChargeRepository;
    }

    public List<OrderChargeResponse> mapOrderCharges(Set<OrderCharge> orderCharges) {
        return orderCharges.stream()
                .map(charge -> OrderChargeResponse.newBuilder()
                        .setChargeType(charge.getChargeType().name())
                        .setChargeCost(charge.getChargeCost())
                        .build())
                .toList();
    }

    public List<OrderCharge> createOrderCharges(List<OrderChargeRequest> orderChargeRequests, Order order){
        List<OrderCharge> charges =  orderChargeRequests.stream().map(orderChargeRequest -> {
            OrderCharge orderCharge = new OrderCharge();
            orderCharge.setOrder(order);
            orderCharge.setChargeType(ChargeType.valueOf(orderChargeRequest.getChargeType()));
            orderCharge.setChargeCost(new Money(orderChargeRequest.getChargeCost()));
            return orderCharge;
        }).toList();
        orderChargeRepository.saveAll(charges);
        return charges;
    }
}
