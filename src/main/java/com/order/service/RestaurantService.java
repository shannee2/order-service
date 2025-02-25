package com.order.service;

import com.order.dto.order.CreateOrderRequest;
import com.order.dto.order.CreateOrderResponse;
import com.order.dto.order.OrderItemRequest;
import com.order.dto.restaurant.RestaurantResponse;
//import com.order.grpc.CreateOrderRequest;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final AuthService authService;
    private final RestTemplate restTemplate;


    public RestaurantService(AuthService authService, RestTemplate restTemplate) {
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    private static final String CATALOG_SERVICE_URL = "http://localhost:8080/restaurants/";

    public CreateOrderResponse validateRestaurantAndItemsDetails(CreateOrderRequest request) {
        String token = authService.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RestaurantResponse> responseEntity = restTemplate.exchange(
                CATALOG_SERVICE_URL + request.getRestaurantId(),
                HttpMethod.GET,
                entity,
                RestaurantResponse.class
        );

        RestaurantResponse restaurantResponse = responseEntity.getBody();
        assert restaurantResponse != null;
        return new CreateOrderResponse(
                new CreateOrderResponse.RestaurantInfo(restaurantResponse.getId(), restaurantResponse.getName()),
                populateOrderItems(restaurantResponse.getMenuItems(), request.getItems())
        );
    }

    public List<CreateOrderResponse.OrderItemInfo> populateOrderItems(List<RestaurantResponse.MenuItemResponse> menuItems, List<OrderItemRequest> items) {
        List<CreateOrderResponse.OrderItemInfo> orderItemInfos = new ArrayList<>();
        for(OrderItemRequest item : items) {
            for(RestaurantResponse.MenuItemResponse menuItem : menuItems) {
                if(menuItem.getId().equals(item.getItemId())) {
                    orderItemInfos.add(new CreateOrderResponse.OrderItemInfo(menuItem.getId(), menuItem.getName()));
                    break;
                }
            }
        }
        if(orderItemInfos.size() != items.size()) {
            throw new IllegalArgumentException("Invalid item ID");
        }
        return orderItemInfos;
    }
}
