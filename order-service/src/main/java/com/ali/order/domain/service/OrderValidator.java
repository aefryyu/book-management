package com.ali.order.domain.service;

import com.ali.order.client.catalog.Product;
import com.ali.order.client.catalog.ProductServiceClient;
import com.ali.order.domain.dto.CreateOrderRequest;
import com.ali.order.domain.exception.InvalidOrderException;
import com.ali.order.domain.model.OrderItem;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final ProductServiceClient client;
    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    public OrderValidator(ProductServiceClient client) {
        this.client = client;
    }

    void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (OrderItem item : items) {
            Product product = client.getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid item code : " + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                log.error(
                        "Product price not matching. Actual price:{}, received price:{}",
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
