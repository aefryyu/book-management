package com.ali.order.domain.service;

import com.ali.order.ApplicationProperties;
import com.ali.order.domain.event.OrderCanceledEvent;
import com.ali.order.domain.event.OrderCreatedEvent;
import com.ali.order.domain.event.OrderDeliveredEvent;
import com.ali.order.domain.event.OrderErrorEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public OrderEventPublisher(ApplicationProperties properties, RabbitTemplate rabbitTemplate) {
        this.properties = properties;
        this.rabbitTemplate = rabbitTemplate;
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
            String jsonPayload = new ObjectMapper().writeValueAsString(payload);
            log.info(
                    "SENDING to exchange [{}] with routingKey [{}]: {}",
                    properties.orderEventExchange(),
                    routingKey,
                    jsonPayload);
            rabbitTemplate.convertAndSend(properties.orderEventExchange(), routingKey, payload);
        } catch (JsonProcessingException e) {
            log.error("ERROR serializing payload: {}", e.getMessage());
        }

    }
}
