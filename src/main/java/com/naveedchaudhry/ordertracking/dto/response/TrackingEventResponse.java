package com.naveedchaudhry.ordertracking.dto.response;

import com.naveedchaudhry.ordertracking.model.OrderStatus;

import java.time.LocalDateTime;

public record TrackingEventResponse(
        Long id,
        OrderStatus status,
        String location,
        String remarks,
        LocalDateTime timestamp
) {
}
