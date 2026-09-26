package com.naveedchaudhry.ordertracking.dto.request;

import com.naveedchaudhry.ordertracking.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull(message = "newStatus is required")
        OrderStatus newStatus,

        String location,   // optional — where the package is now
        String remarks     // optional — a free-text note
) {
}
