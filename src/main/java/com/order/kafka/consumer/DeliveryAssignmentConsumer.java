package com.order.kafka.consumer;

import com.order.dto.order.DeliveryAssignmentDto;
import com.order.model.enums.OrderStatus;
import com.order.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeliveryAssignmentConsumer {

    private final  OrderService orderService;
    private final ObjectMapper objectMapper; // To deserialize JSON

    @Autowired
    public DeliveryAssignmentConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "delivery_assignment", groupId = "order-service-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String message = record.value();
            System.out.println("Received message: " + message);

            // Deserialize JSON
            DeliveryAssignmentDto assignment = objectMapper.readValue(message, DeliveryAssignmentDto.class);

            // Process the delivery assignment
            orderService.updateOrderStatus(assignment.getOrderId(), OrderStatus.DELIVERY_PARTNER_ASSIGNED);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
