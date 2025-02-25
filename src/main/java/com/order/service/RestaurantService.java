package com.order.service;


import com.order.dto.restaurant.RestaurantResponse;
//import com.order.grpc.CreateOrderRequest;
import com.order.exceptions.order.InvalidItemIdException;
import com.order.grpc.CreateOrderRequest;
import com.order.grpc.CreateOrderResponse;
import com.order.grpc.OrderItemRequest;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantService {

    private final AuthService authService;
    private final RestTemplate restTemplate;


    public RestaurantService(AuthService authService, RestTemplate restTemplate) {
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    private static final String CATALOG_SERVICE_URL = "http://localhost:8080/restaurants/";

    public void validateRestaurantAndItemsDetails(CreateOrderRequest request) {
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


        for(OrderItemRequest item : request.getItemsList()) {
            boolean found = false;
            for(RestaurantResponse.MenuItemResponse menuItem : Objects.requireNonNull(responseEntity.getBody()).getMenuItems()) {
                if(menuItem.getId().equals(item.getItemId())) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                throw new InvalidItemIdException("Invalid item ID: "+item.getItemId());
            }
        }

        RestaurantResponse restaurantResponse = responseEntity.getBody();
        assert restaurantResponse != null;
    }
}
