package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.Order;
import com.codeisevenlycooked.evenly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);

    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUser(Long id, User user);
}
