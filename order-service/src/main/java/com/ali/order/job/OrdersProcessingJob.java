package com.ali.order.job;

import com.ali.order.domain.service.OrderService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrdersProcessingJob {

    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrdersProcessingJob.class);

    public OrdersProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${orders.new-orders-job-cron}")
    @SchedulerLock(name = "processNewOrders")
    public void processNewOrders() {
        LockAssert.assertLocked();
        log.info("Publishing order events at {}", Instant.now());

        orderService.processNewOrders();
    }
}
