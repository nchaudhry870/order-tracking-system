package com.naveedchaudhry.ordertracking.dto.response;

import com.naveedchaudhry.ordertracking.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerId,
        String customerName,
        String shippingAddress,
        OrderStatus status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemResponse> items
) {
}
