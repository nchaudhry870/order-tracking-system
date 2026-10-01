package com.naveedchaudhry.ordertracking;

import com.naveedchaudhry.ordertracking.dto.request.CreateOrderRequest;
import com.naveedchaudhry.ordertracking.dto.request.OrderItemRequest;
import com.naveedchaudhry.ordertracking.dto.request.UpdateOrderStatusRequest;
import com.naveedchaudhry.ordertracking.dto.response.OrderResponse;
import com.naveedchaudhry.ordertracking.exception.InvalidStatusTransitionException;
import com.naveedchaudhry.ordertracking.model.OrderStatus;
import com.naveedchaudhry.ordertracking.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class OrderTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderTrackingApplication.class, args);
    }

    // Temporary: verify the data layer works. We'll remove this in a later part.
    @Bean
    CommandLineRunner demo(OrderService orderService) {
        return args -> {
            // Create an order through the service
            var request = new CreateOrderRequest(
                    "CUST-00042", "Ada Lovelace", "10 Analytical Ave, London",
                    List.of(
                            new OrderItemRequest("Wireless Mouse", 2, new BigDecimal("24.99")),
                            new OrderItemRequest("Mechanical Keyboard", 1, new BigDecimal("79.99"))
                    ));
            OrderResponse created = orderService.createOrder(request);
            System.out.println("Created order " + created.id()
                    + " total=" + created.totalAmount() + " status=" + created.status());

            // A legal move: CREATED -> CONFIRMED
            orderService.updateOrderStatus(created.id(),
                    new UpdateOrderStatusRequest(OrderStatus.CONFIRMED, "Warehouse A", "Payment verified"));

            // An illegal move: CONFIRMED -> DELIVERED (should be rejected)
            try {
                orderService.updateOrderStatus(created.id(),
                        new UpdateOrderStatusRequest(OrderStatus.DELIVERED, null, null));
            } catch (InvalidStatusTransitionException ex) {
                System.out.println("Correctly rejected: " + ex.getMessage());
            }

            // The timeline so far
            orderService.getTrackingHistory(created.id()).timeline()
                    .forEach(e -> System.out.println("  " + e.timestamp() + " → " + e.status()));
        };

    }
}
