package com.order.service;

//import com.order.dto.catalog.RestaurantResponse;
import com.order.dto.order.CreateOrderRequest;
import com.order.dto.order.CreateOrderResponse;
import com.order.dto.order.OrderChargeRequest;
import com.order.dto.order.OrderItemRequest;
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

import java.util.List;
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
            orderCharge.setChargeType(orderChargeRequest.getChargeType());
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

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        CreateOrderResponse createOrderResponse = restaurantService.validateRestaurantAndItemsDetails(request);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setGrandTotal(new Money(request.getGrandTotal(), request.getCurrency()));
        order.setCreatedBy(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());

        order = orderRepository.save(order);

        List<OrderItem> orderItems = createOrderItems(request.getItems(), order);
        List<OrderCharge> orderCharges = createOrderCharges(request.getCharges(), order);
        OrderHistory orderHistory = createOrderHistory(OrderStatus.ORDER_CREATED, order);

        order.addHistory(orderHistory);
        order.setOrderItems(orderItems);
        order.setOrderCharges(orderCharges);

        orderItemRepository.saveAll(orderItems);
        orderChargeRepository.saveAll(orderCharges);
        orderHistoryRepository.save(orderHistory);

        createOrderResponse.setOrderId(order.getOrderId());

        return createOrderResponse;
    }
}
