package com.ali.order.domain.service;

import com.ali.order.domain.dto.CreateOrderRequest;
import com.ali.order.domain.dto.CreateOrderResponse;
import com.ali.order.domain.dto.OrderDto;
import com.ali.order.domain.dto.OrderSummary;
import com.ali.order.domain.entity.OrderEntity;
import com.ali.order.domain.event.OrderCreatedEvent;
import com.ali.order.domain.mapper.OrderEventMapper;
import com.ali.order.domain.mapper.OrderMapper;
import com.ali.order.domain.model.*;
import com.ali.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("USA", "GERMANY", "UK", "IDN");
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(
            OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity saveOrder = this.orderRepository.save(newOrder);

        log.info("Created order with number : {}", saveOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(saveOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(saveOrder.getOrderNumber());
    }

    public List<OrderSummary> findOrders(String userName) {
        return orderRepository.findByUsername(userName);
    }

    public Optional<OrderDto> findUserOrder(String userName, String orderNumber) {
        return orderRepository
                .findByUserNameAndOrderNumber(userName, orderNumber)
                .map(OrderMapper::convertToDto);
    }

    public void processNewOrders() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("found {} new order process", orders.size());
        for (OrderEntity order : orders) {
            this.process(order);
        }
    }

    private void process(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                log.info("Order number {} can be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                log.info("Order number {} can`t be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELED);
                orderEventService.save(
                        OrderEventMapper.buildOrderCanceledEvent(order, "Can`t deliver to the location"));
            }
        } catch (RuntimeException e) {
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.getDeliveryAddress().country().toUpperCase());
    }
}
