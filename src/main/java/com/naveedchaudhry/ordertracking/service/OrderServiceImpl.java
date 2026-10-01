package com.naveedchaudhry.ordertracking.service;

import com.naveedchaudhry.ordertracking.dto.request.CreateOrderRequest;
import com.naveedchaudhry.ordertracking.dto.request.UpdateOrderStatusRequest;
import com.naveedchaudhry.ordertracking.dto.response.OrderResponse;
import com.naveedchaudhry.ordertracking.dto.response.OrderTrackingResponse;
import com.naveedchaudhry.ordertracking.exception.InvalidStatusTransitionException;
import com.naveedchaudhry.ordertracking.exception.OrderNotFoundException;
import com.naveedchaudhry.ordertracking.mapper.OrderMapper;
import com.naveedchaudhry.ordertracking.model.Order;
import com.naveedchaudhry.ordertracking.model.OrderStatus;
import com.naveedchaudhry.ordertracking.model.OrderStatusTransitions;
import com.naveedchaudhry.ordertracking.model.TrackingEvent;
import com.naveedchaudhry.ordertracking.repository.OrderRepository;
import com.naveedchaudhry.ordertracking.repository.TrackingEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TrackingEventRepository trackingEventRepository;

    public OrderServiceImpl(OrderRepository orderRepository, TrackingEventRepository trackingEventRepository) {
        this.orderRepository = orderRepository;
        this.trackingEventRepository = trackingEventRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Map request -> entity (status = CREATED, totalAmount computed)
        Order order = OrderMapper.toEntity(request);

        // Business rule: every new order begins its history with a CREATED event
        order.addTrackingEvent(
                new TrackingEvent(OrderStatus.CREATED, null, "Order placed"));

        Order saved = orderRepository.save(order);   // cascade saves items + event
        return OrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderStatus current = order.getStatus();
        OrderStatus next = request.newStatus();

        // The core business rule — reject illegal transitions
        if (!OrderStatusTransitions.isAllowed(current, next)) {
            throw new InvalidStatusTransitionException(current, next);
        }

        order.setStatus(next);
        order.addTrackingEvent(
                new TrackingEvent(next, request.location(), request.remarks()));

        Order saved = orderRepository.save(order);
        return OrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderTrackingResponse getTrackingHistory(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        List<TrackingEvent> events =
                trackingEventRepository.findByOrderIdOrderByTimestampAsc(orderId);

        return OrderMapper.toTrackingResponse(order, events);
    }
}
