package com.order.grpc.service;

import com.order.grpc.CreateOrderRequest;
import com.order.grpc.CreateOrderResponse;
import com.order.grpc.OrderChargeRequest;
import com.order.grpc.OrderServiceGrpc;
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
import com.order.service.OrderService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderChargeRepository orderChargeRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderService orderService;

    public OrderGrpcService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            OrderChargeRepository orderChargeRepository, OrderHistoryRepository orderHistoryRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderChargeRepository = orderChargeRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {
        Long orderId = orderService.createOrder(request);

        CreateOrderResponse response = CreateOrderResponse.newBuilder()
                .setOrderId(orderId)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}