package com.naveedchaudhry.ordertracking.service;

import com.naveedchaudhry.ordertracking.dto.request.CreateOrderRequest;
import com.naveedchaudhry.ordertracking.dto.request.UpdateOrderStatusRequest;
import com.naveedchaudhry.ordertracking.dto.response.OrderResponse;
import com.naveedchaudhry.ordertracking.dto.response.OrderTrackingResponse;


import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByCustomerId(String customerId);
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);
    OrderTrackingResponse getTrackingHistory(Long orderId);
}
