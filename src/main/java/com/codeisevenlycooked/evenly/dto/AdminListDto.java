package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AdminListDto {
    private Long id;
    private String orderNumber;
    private String userId;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createAt;
}
