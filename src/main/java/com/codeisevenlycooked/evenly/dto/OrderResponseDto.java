package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String orderNumber;
    private BigDecimal totalPrice;
    private String status;
    private String receiverName;
    private String address;
    private String mobile;
    private String deliveryMessage;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> orderItems;
}
