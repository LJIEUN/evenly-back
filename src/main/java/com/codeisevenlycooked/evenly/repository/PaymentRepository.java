package com.codeisevenlycooked.evenly.repository;
import com.codeisevenlycooked.evenly.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
}
