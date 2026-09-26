package com.naveedchaudhry.ordertracking.dto.response;

import com.naveedchaudhry.ordertracking.model.OrderStatus;

import java.util.List;

public record OrderTrackingResponse(
    Long orderId,
    OrderStatus currentStatus,
    List<TrackingEventResponse> timeline
)
{
}
