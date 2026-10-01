package com.naveedchaudhry.ordertracking.exception;

import com.naveedchaudhry.ordertracking.model.OrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(OrderStatus from, OrderStatus to) {
        super("Invalid status transition from " + from + " to " + to);
    }
}
