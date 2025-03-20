package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
}
