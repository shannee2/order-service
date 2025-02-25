package com.order.service;

//import com.order.dto.catalog.RestaurantResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderChargeRepository orderChargeRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final RestaurantService restaurantService;

    private static final String CATALOG_SERVICE_URL = "http://localhost:8080/restaurants/";
//    private final CurrencyService currencyService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        OrderChargeRepository orderChargeRepository, OrderHistoryRepository orderHistoryRepository,
                        RestTemplate restTemplate, AuthService authService, RestaurantService restaurantService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderChargeRepository = orderChargeRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.restaurantService = restaurantService;
    }

    @Transactional
    public Long createOrder(CreateOrderRequest request) {
        restaurantService.validateRestaurantAndItemsDetails(request);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setGrandTotal(new Money(request.getGrandTotal(), request.getCurrency()));
        order.setCreatedBy(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());

        order = orderRepository.save(order);

        Set<OrderItem> orderItems = new HashSet<>(createOrderItems(request.getItemsList(), order));
        Set<OrderCharge> orderCharges = new HashSet<>(createOrderCharges(request.getChargesList(), order));
        OrderHistory orderHistory = createOrderHistory(OrderStatus.ORDER_CREATED, order);

        order.addHistory(orderHistory);
        order.setOrderItems(orderItems);
        order.setOrderCharges(orderCharges);

        orderItemRepository.saveAll(orderItems);
        orderChargeRepository.saveAll(orderCharges);
        orderHistoryRepository.save(orderHistory);


        return order.getOrderId();
    }

    public GetOrderResponse getOrderDetails(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return GetOrderResponse.newBuilder()
                .setOrderId(order.getOrderId())
                .setOrderStatus(order.getOrderStatus().name())
                .setGrandTotal(order.getGrandTotal().getAmount())
                .setCurrency(order.getGrandTotal().getCurrency())
                .setCreatedBy(order.getCreatedBy())
                .setRestaurantId(order.getRestaurantId())
                .addAllItems(mapOrderItems(order.getOrderItems()))
                .addAllCharges(mapOrderCharges(order.getOrderCharges()))
                .build();
    }

    private List<OrderItemResponse> mapOrderItems(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> OrderItemResponse.newBuilder()
                        .setItemId(item.getOrderItemExternalId())
                        .setQuantity(item.getQuantity())
                        .setUnitPrice(item.getUnitPrice())
                        .build())
                .toList();
    }

    private List<OrderChargeResponse> mapOrderCharges(Set<OrderCharge> orderCharges) {
        return orderCharges.stream()
                .map(charge -> OrderChargeResponse.newBuilder()
                        .setChargeType(charge.getChargeType().name())
                        .setChargeCost(charge.getChargeCost())
                        .build())
                .toList();
    }

    public List<OrderItem> createOrderItems(List<OrderItemRequest> orderItemRequests, Order order){
        return orderItemRequests.stream().map(orderItemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setOrderItemExternalId(orderItemRequest.getItemId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setUnitPrice(new Money(orderItemRequest.getUnitPrice()));
            return orderItem;
        }).collect(Collectors.toList());
    }

    public List<OrderCharge> createOrderCharges(List<OrderChargeRequest> orderChargeRequests, Order order){
        return orderChargeRequests.stream().map(orderChargeRequest -> {
            OrderCharge orderCharge = new OrderCharge();
            orderCharge.setOrder(order);
            orderCharge.setChargeType(ChargeType.valueOf(orderChargeRequest.getChargeType()));
            orderCharge.setChargeCost(new Money(orderChargeRequest.getChargeCost()));
            return orderCharge;
        }).collect(Collectors.toList());
    }

    public OrderHistory createOrderHistory(OrderStatus orderStatus, Order order){
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatusId(orderStatus);
        orderHistory.setStatusDate(java.time.LocalDateTime.now());
        return orderHistory;
    }

}