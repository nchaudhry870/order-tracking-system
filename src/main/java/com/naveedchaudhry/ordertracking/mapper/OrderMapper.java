package com.naveedchaudhry.ordertracking.mapper;

import com.naveedchaudhry.ordertracking.dto.request.CreateOrderRequest;
import com.naveedchaudhry.ordertracking.dto.response.OrderItemResponse;
import com.naveedchaudhry.ordertracking.dto.response.OrderResponse;
import com.naveedchaudhry.ordertracking.dto.response.OrderTrackingResponse;
import com.naveedchaudhry.ordertracking.dto.response.TrackingEventResponse;
import com.naveedchaudhry.ordertracking.model.Order;
import com.naveedchaudhry.ordertracking.model.OrderItem;
import com.naveedchaudhry.ordertracking.model.OrderStatus;
import com.naveedchaudhry.ordertracking.model.TrackingEvent;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {
    private OrderMapper() { }   // utility class — no instances

    // ---- Request DTO -> Entity ----

    public static Order toEntity(CreateOrderRequest request) {

        BigDecimal totalAmount = request.items().stream()
                .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order(
                request.customerId(),
                request.customerName(),
                request.shippingAddress(),
                OrderStatus.CREATED,
                totalAmount
        );

        request.items().forEach(i -> order.addItem(new OrderItem(i.productName(), i.quantity(), i.unitPrice())));

        return order;
    }

    // ---- Entity -> Response DTO ----

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderMapper::toItemResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getShippingAddress(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items);
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice());
    }

    public static TrackingEventResponse toEventResponse(TrackingEvent event) {
        return new TrackingEventResponse(
                event.getId(),
                event.getStatus(),
                event.getLocation(),
                event.getRemarks(),
                event.getTimestamp());
    }

    public static OrderTrackingResponse toTrackingResponse(Order order,
                                                           List<TrackingEvent> events) {
        List<TrackingEventResponse> timeline = events.stream()
                .map(OrderMapper::toEventResponse)
                .toList();

        return new OrderTrackingResponse(order.getId(), order.getStatus(), timeline);
    }




}
