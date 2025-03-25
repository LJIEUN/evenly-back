package com.codeisevenlycooked.evenly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus status = OrderStatus.PAYMENT_PENDING;

    private String receiverName;
    private String address;
    private String mobile;
    @Column(length = 5000)
    private String deliveryMessage;
    private String paymentMethod;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments;


    public Order(String orderNumber, User user, BigDecimal totalPrice, String receiverName, String address, String mobile, String deliveryMessage, String paymentMethod) {
        this.orderNumber = orderNumber;
        this.user = user;
        this.totalPrice = totalPrice;
        this.receiverName = receiverName;
        this.address = address;
        this.mobile = mobile;
        this.deliveryMessage = deliveryMessage;
        this.paymentMethod = paymentMethod;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void changeStatus (OrderStatus newStatus) {
        this.status = newStatus;
    }
}
