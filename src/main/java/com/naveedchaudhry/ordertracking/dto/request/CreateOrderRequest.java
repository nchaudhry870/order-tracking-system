package com.naveedchaudhry.ordertracking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "customerId is required")
        String customerId,

        @NotBlank(message = "customerName is required")
        String customerName,

        @NotBlank(message = "shippingAddress is required")
        String shippingAddress,

        @NotEmpty(message = "an order must have at least one item")
        @Valid
        List<OrderItemRequest> items
)
{
}
