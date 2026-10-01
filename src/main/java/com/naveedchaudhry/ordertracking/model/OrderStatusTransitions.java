package com.naveedchaudhry.ordertracking.model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.naveedchaudhry.ordertracking.model.OrderStatus.*;

public final class OrderStatusTransitions {

    // For each status, the set of statuses it may legally move to next.
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new EnumMap<>(OrderStatus.class);

    static {
        ALLOWED.put(CREATED, EnumSet.of(CONFIRMED, CANCELLED));
        ALLOWED.put(CONFIRMED, EnumSet.of(PACKED, CANCELLED));
        ALLOWED.put(PACKED,           EnumSet.of(SHIPPED, CANCELLED));
        ALLOWED.put(SHIPPED,          EnumSet.of(IN_TRANSIT));
        ALLOWED.put(IN_TRANSIT,       EnumSet.of(OUT_FOR_DELIVERY, DELAYED));
        ALLOWED.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED, FAILED_DELIVERY));
        ALLOWED.put(DELAYED,          EnumSet.of(IN_TRANSIT, OUT_FOR_DELIVERY));
        ALLOWED.put(FAILED_DELIVERY,  EnumSet.of(OUT_FOR_DELIVERY, CANCELLED));
        ALLOWED.put(DELIVERED,        EnumSet.noneOf(OrderStatus.class)); // terminal
        ALLOWED.put(CANCELLED,        EnumSet.noneOf(OrderStatus.class)); // terminal
    }

    private OrderStatusTransitions() { }   // utility class — no instances

    public static boolean isAllowed(OrderStatus from, OrderStatus to) {
        return ALLOWED.getOrDefault(from, EnumSet.noneOf(OrderStatus.class)).contains(to);
    }


}
