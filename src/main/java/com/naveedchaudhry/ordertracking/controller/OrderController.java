package com.naveedchaudhry.ordertracking.controller;

import com.naveedchaudhry.ordertracking.dto.request.CreateOrderRequest;
import com.naveedchaudhry.ordertracking.dto.request.UpdateOrderStatusRequest;
import com.naveedchaudhry.ordertracking.dto.response.OrderResponse;
import com.naveedchaudhry.ordertracking.dto.response.OrderTrackingResponse;
import com.naveedchaudhry.ordertracking.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /api/orders
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }

    // GET /api/orders/{id}
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    // GET /api/orders?customerId=CUST-00042
    @GetMapping
    public List<OrderResponse> getOrdersByCustomer(@RequestParam String customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    // PATCH /api/orders/{id}/status
    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(id, request);
    }

    // GET /api/orders/{id}/tracking
    @GetMapping("/{id}/tracking")
    public OrderTrackingResponse getTracking(@PathVariable Long id) {
        return orderService.getTrackingHistory(id);
    }

}
