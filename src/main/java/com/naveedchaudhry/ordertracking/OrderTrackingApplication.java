package com.naveedchaudhry.ordertracking;

import com.naveedchaudhry.ordertracking.model.Order;
import com.naveedchaudhry.ordertracking.model.OrderItem;
import com.naveedchaudhry.ordertracking.model.OrderStatus;
import com.naveedchaudhry.ordertracking.model.TrackingEvent;
import com.naveedchaudhry.ordertracking.repository.OrderRepository;
import com.naveedchaudhry.ordertracking.repository.TrackingEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class OrderTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderTrackingApplication.class, args);
    }

    // Temporary: verify the data layer works. We'll remove this in a later part.
    @Bean
    CommandLineRunner demo(OrderRepository orders, TrackingEventRepository events) {
        return args -> {
            // Build an order with two items
            Order order = new Order(
                    "CUST-00042",
                    "Ada Lovelace",
                    "10 Analytical Ave, London",
                    OrderStatus.CREATED,
                    new BigDecimal("129.97"));

            // 2 x 24.99 = 49.98, plus 1 x 79.99  ->  129.97, the total above
            order.addItem(new OrderItem("Wireless Mouse", 2, new BigDecimal("24.99")));
            order.addItem(new OrderItem("Mechanical Keyboard", 1, new BigDecimal("79.99")));

            // Record the first tracking event
            order.addTrackingEvent(
                    new TrackingEvent(OrderStatus.CREATED, "Warehouse A", "Order placed"));

            // One save persists the order, its items, and its event (cascade)
            Order saved = orders.save(order);
            System.out.println("Saved order with id = " + saved.getId());

            // Read it back
            System.out.println("Total orders in DB: " + orders.count());
            System.out.println("Orders for CUST-00042: "
                    + orders.findByCustomerIdOrderByCreatedAtDesc("CUST-00042").size());

            //The custom @Query
            System.out.println("Orders above 100: " + orders.findOrdersAbove(new BigDecimal("100.00")).size());

            // Read the tracking timeline
            events.findByOrderIdOrderByTimestampAsc(saved.getId())
                    .forEach(e -> System.out.println("  " + e.getTimestamp()
                            + " → " + e.getStatus() + " @ " + e.getLocation()));
        };

    }
}
