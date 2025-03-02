package com.order.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.order.OrderDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String ORDER_TOPIC = "orders";
    private final ObjectMapper objectMapper;


    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrder(OrderDto order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(ORDER_TOPIC, orderJson);
            System.out.println("Published Order Event: " + orderJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
