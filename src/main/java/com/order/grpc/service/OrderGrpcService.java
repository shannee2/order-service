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
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderChargeRepository orderChargeRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderGrpcService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            OrderChargeRepository orderChargeRepository, OrderHistoryRepository orderHistoryRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderChargeRepository = orderChargeRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {

        com.order.dto.order.CreateOrderRequest requestDto = new com.order.dto.order.CreateOrderRequest();
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setGrandTotal(new Money(request.getGrandTotal(), request.getCurrency()));
        order.setCreatedBy(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());

        order = orderRepository.save(order);

        Order finalOrder = order;
        List<OrderItem> orderItems = request.getItemsList().stream().map(itemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemExternalId(itemRequest.getItemId());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(itemRequest.getUnitPrice());
            orderItem.setOrder(finalOrder);
            return orderItem;
        }).collect(Collectors.toList());

        Order finalOrder1 = order;
        for(OrderChargeRequest req : request.getChargesList()){
            System.out.println("CHarge type======>  "+req.getChargeType());
        }
//        System.out.println("Received JSON Request: " + jsonPayload);

        System.out.println("Full request: \n" + request);


        List<OrderCharge> orderCharges = request.getChargesList().stream().map(chargeRequest -> {
            OrderCharge orderCharge = new OrderCharge();
            orderCharge.setChargeType(ChargeType.valueOf(chargeRequest.getChargeType()));
            orderCharge.setChargeCost(new Money(chargeRequest.getChargeCost(), null));
            orderCharge.setOrder(finalOrder1);
            return orderCharge;
        }).collect(Collectors.toList());

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatusId(OrderStatus.ORDER_CREATED);
        orderHistory.setStatusDate(java.time.LocalDateTime.now());

        order.setOrderItems(orderItems);
        order.setOrderCharges(orderCharges);
        order.addHistory(orderHistory);

        orderItemRepository.saveAll(orderItems);
        orderChargeRepository.saveAll(orderCharges);
        orderHistoryRepository.save(orderHistory);

        CreateOrderResponse response = CreateOrderResponse.newBuilder()
                .setOrderId(order.getOrderId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
