package com.naveedchaudhry.ordertracking.model;

public enum OrderStatus {
    //Happy path
    CREATED,
    CONFIRMED,
    PACKED,
    SHIPPED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,

    // Exception states
    DELAYED,
    CANCELLED,
    FAILED_DELIVERY
}
