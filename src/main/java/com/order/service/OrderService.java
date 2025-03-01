package com.order.service;

//import com.order.dto.catalog.RestaurantResponse;
import com.order.dto.order.OrderDto;
import com.order.dto.restaurant.RestaurantResponse;
import com.order.grpc.*;
import com.order.kafka.OrderProducer;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderChargeRepository orderChargeRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final RestaurantService restaurantService;
    private final OrderItemService orderItemService;
    private final OrderChargeService orderChargeService;
    private final OrderHistoryService orderHistoryService;
    private final OrderProducer orderProducer;

    private static final String CATALOG_SERVICE_URL = "http://localhost:8080/restaurants/";

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        OrderChargeRepository orderChargeRepository, OrderHistoryRepository orderHistoryRepository,
                        RestTemplate restTemplate, AuthService authService, RestaurantService restaurantService, OrderItemService orderItemService, OrderChargeService orderChargeService, OrderHistoryService orderHistoryService, KafkaTemplate<String, String> kafkaTemplate, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderChargeRepository = orderChargeRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.restaurantService = restaurantService;
        this.orderItemService = orderItemService;
        this.orderChargeService = orderChargeService;
        this.orderHistoryService = orderHistoryService;
        this.orderProducer = orderProducer;
    }

    @Transactional
    public Long createOrder(CreateOrderRequest request) {
        RestaurantResponse restaurantResponse = restaurantService.validateRestaurantAndItemsDetails(request);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setGrandTotal(new Money(request.getGrandTotal(), request.getCurrency()));
        order.setCreatedBy(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());

        order = orderRepository.save(order);

        Set<OrderItem> orderItems = new HashSet<>(orderItemService.createOrderItems(request.getItemsList(), order));
        Set<OrderCharge> orderCharges = new HashSet<>(orderChargeService.createOrderCharges(request.getChargesList(), order));
        OrderHistory orderHistory = orderHistoryService.createOrderHistory(order, OrderStatus.ORDER_CREATED);

        order.addHistory(orderHistory);
        order.setOrderItems(orderItems);
        order.setOrderCharges(orderCharges);

        orderItemRepository.saveAll(orderItems);
        orderChargeRepository.saveAll(orderCharges);
        orderHistoryRepository.save(orderHistory);


//        for(OrderItem item : order.getOrderItems()){
//            System.out.println("ORDER ITEM ID: "+item.getOrderItemExternalId());
//        }

        orderProducer.sendOrder(new OrderDto(order, restaurantResponse));

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
                .addAllItems(orderItemService.mapOrderItems(order.getOrderItems()))
                .addAllCharges(orderChargeService.mapOrderCharges(order.getOrderCharges()))
                .build();
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findWithHistoriesByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderHistory orderHistory = orderHistoryService.createOrderHistory(order, orderStatus);
        order.addHistory(orderHistory);
        order.setOrderStatus(orderStatus);
//        orderRepository.save(order);
        orderHistory.setOrder(order);
        orderHistoryRepository.save(orderHistory);
    }
}