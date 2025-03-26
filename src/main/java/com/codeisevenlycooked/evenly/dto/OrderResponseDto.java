package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.OrderStatus;
import com.codeisevenlycooked.evenly.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private OrderCreationResponseDto orderInfo;

    private String orderNumber;
    private OrderStatus status;
    private String receiverName;
    private String address;
    private String mobile;
    private String deliveryMessage;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
}
