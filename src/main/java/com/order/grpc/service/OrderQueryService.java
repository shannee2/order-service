package com.order.grpc.service;

import com.order.grpc.*;
import com.order.model.order.Order;
import com.order.repository.OrderRepository;
import com.order.service.OrderService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class OrderQueryService extends OrderQueryServiceGrpc.OrderQueryServiceImplBase {

    @Autowired
    private OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderQueryService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void getOrderDetails(GetOrderRequest request, StreamObserver<GetOrderResponse> responseObserver) {

        GetOrderResponse response = orderService.getOrderDetails(request.getOrderId());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
