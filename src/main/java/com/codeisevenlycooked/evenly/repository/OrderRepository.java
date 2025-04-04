package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.Order;
import com.codeisevenlycooked.evenly.entity.OrderStatus;
import com.codeisevenlycooked.evenly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByOrderNumberAndUser(String orderNumber, User user);
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus orderStatus, LocalDateTime createdAt);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
