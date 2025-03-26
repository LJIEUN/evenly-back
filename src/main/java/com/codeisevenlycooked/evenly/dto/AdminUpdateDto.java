package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.OrderItem;
import com.codeisevenlycooked.evenly.entity.OrderStatus;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AdminUpdateDto {
    private Long id;
    private String orderNumber;
    private String userId;
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createAt;
    private String receiverName;
    private String address;
    private String mobile;
    private String deliveryMessage;
    private String paymentMethod;
}
