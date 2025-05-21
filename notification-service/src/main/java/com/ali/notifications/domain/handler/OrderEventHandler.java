package com.ali.notifications.domain.handler;

import com.ali.notifications.domain.entity.OrderEventEntity;
import com.ali.notifications.domain.event.OrderCanceledEvent;
import com.ali.notifications.domain.event.OrderCreatedEvent;
import com.ali.notifications.domain.event.OrderDeliveredEvent;
import com.ali.notifications.domain.event.OrderErrorEvent;
import com.ali.notifications.domain.repository.OrderEventRepository;
import com.ali.notifications.domain.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);
    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    public OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleCreatedEvent(OrderCreatedEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("received duplicated orderCreatedEvent with id: {}", event.eventId());
            return;
        }
        log.info("received orderCreatedEvent with id: {}", event.eventId());
        notificationService.sendOrderCreatedNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handle(OrderDeliveredEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("received duplicated orderDeliveredEvent with id: {}", event.eventId());
            return;
        }
        log.info("received orderDeliveredEvent with id: {}", event.orderNumber());
        notificationService.sendOrderDeliveredNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.canceled-orders-queue}")
    void handle(OrderCanceledEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("received duplicated orderCanceledEvent with id: {}", event.eventId());
            return;
        }
        log.info("received orderCanceledEvent with id: {}", event.orderNumber());
        notificationService.sendOrderCancelledNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handle(OrderErrorEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("received duplicated orderErrorEvent with id: {}", event.eventId());
            return;
        }
        log.info("received orderErrorEvent with id: {}", event.orderNumber());
        notificationService.sendOrderErrorEventNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }
}
