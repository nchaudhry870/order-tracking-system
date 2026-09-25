package com.naveedchaudhry.ordertracking.repository;

import com.naveedchaudhry.ordertracking.model.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    // The tracking timeline: all events for one order, oldest first
    List<TrackingEvent> findByOrderIdOrderByTimestampAsc(Long orderId);
}
