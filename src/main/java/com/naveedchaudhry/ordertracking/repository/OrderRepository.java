package com.naveedchaudhry.ordertracking.repository;

import com.naveedchaudhry.ordertracking.model.Order;
import com.naveedchaudhry.ordertracking.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find every order placed by a given customer, newest first
    List<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId);

    // Find all orders currently in a given status
    List<Order> findByStatus(OrderStatus status);

    // A custom JPQL query — explained in Step 3
    @Query("SELECT o FROM Order o WHERE o.totalAmount > :amount")
    List<Order> findOrdersAbove(@Param("amount") BigDecimal amount);
}
