package com.ali.notifications.domain.service;

import com.ali.notifications.ApplicationProperties;
import com.ali.notifications.domain.event.OrderCanceledEvent;
import com.ali.notifications.domain.event.OrderCreatedEvent;
import com.ali.notifications.domain.event.OrderDeliveredEvent;
import com.ali.notifications.domain.event.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final ApplicationProperties properties;
    private final JavaMailSender emailSender;

    public NotificationService(ApplicationProperties properties, JavaMailSender emailSender) {
        this.properties = properties;
        this.emailSender = emailSender;
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        String message =
                """
                        ===================================================
                        Order Created Notification
                        ----------------------------------------------------
                        Dear %s,
                        Your order with orderNumber: %s has been created successfully.

                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Created Notification", message);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        String message =
                """
                        ===================================================
                        Order Delivered Notification
                        ----------------------------------------------------
                        Dear %s,
                        Your order with orderNumber: %s has been delivered successfully.

                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Delivered Notification", message);
    }

    public void sendOrderCancelledNotification(OrderCanceledEvent event) {
        String message =
                """
                        ===================================================
                        Order Cancelled Notification
                        ----------------------------------------------------
                        Dear %s,
                        Your order with orderNumber: %s has been cancelled.
                        Reason: %s

                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Cancelled Notification", message);
    }

    public void sendOrderErrorEventNotification(OrderErrorEvent event) {
        String message =
                """
                        ===================================================
                        Order Processing Failure Notification
                        ----------------------------------------------------
                        Hi Team,
                        The order processing failed for orderNumber: %s.
                        Reason: %s

                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(properties.supportEmail(), "Order Processing Failure Notification", message);
    }

    private void sendEmail(String recipient, String subject, String content) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(recipient);
            helper.setText(content, false);
            helper.setSubject(subject);
            emailSender.send(mimeMessage);
            log.info("Email sent to {} with subject {}", recipient, subject);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending message" + recipient, e);
        }
    }
}
