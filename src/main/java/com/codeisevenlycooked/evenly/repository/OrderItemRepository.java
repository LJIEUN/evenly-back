package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
