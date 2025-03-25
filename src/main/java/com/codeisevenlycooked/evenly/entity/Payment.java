package com.codeisevenlycooked.evenly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime transactionTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;


    public Payment(BigDecimal amount, String paymentMethod, String status, LocalDateTime transactionTime, Order order) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionTime = transactionTime;
        this.order = order;
    }
}
