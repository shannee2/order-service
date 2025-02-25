//package com.order.controller;
//
//import com.order.dto.order.CreateOrderRequest;
//import com.order.dto.order.CreateOrderResponse;
//import com.order.dto.user.UserRequest;
//import com.order.dto.user.UserResponse;
//import com.order.exceptions.users.UserNotFoundException;
//import com.order.model.order.Order;
//import com.order.model.user.User;
//import com.order.service.OrderService;
//import com.order.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.nio.file.AccessDeniedException;
//
//@RestController
//@RequestMapping("/orders")
//public class OrderController {
//
//    private final UserService userService;
//    private final OrderService orderService;
//
//    @Autowired
//    public OrderController(UserService userService, OrderService orderService) {
//        this.userService = userService;
//        this.orderService = orderService;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> getRestaurant(@RequestBody CreateOrderRequest request) {
//        CreateOrderResponse response = orderService.createOrder(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//}