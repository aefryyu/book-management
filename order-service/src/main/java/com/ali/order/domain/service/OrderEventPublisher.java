package com.ali.order.domain.service;

import com.ali.order.ApplicationProperties;
import com.ali.order.domain.event.OrderCanceledEvent;
import com.ali.order.domain.event.OrderCreatedEvent;
import com.ali.order.domain.event.OrderDeliveredEvent;
import com.ali.order.domain.event.OrderErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final ApplicationProperties properties;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventPublisher(
            ApplicationProperties properties, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.properties = properties;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(OrderCreatedEvent event) {
        this.send(properties.newOrdersQueue(), event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCanceledEvent event) {
        this.send(properties.canceledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingKey, Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);

            log.info(
                    "Publishing to exchange '{}' with routing key '{}': {}",
                    properties.orderEventExchange(),
                    routingKey,
                    jsonPayload);

            rabbitTemplate.convertAndSend(properties.orderEventExchange(), routingKey, payload);
            log.info("Message sent successfully");
        } catch (Exception e) {
            log.error("Failed to send message", e);
        }
    }
}
