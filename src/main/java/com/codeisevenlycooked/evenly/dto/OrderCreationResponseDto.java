package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreationResponseDto {

    private Long orderId;

    private List<OrderItemResponseDto> orderItems;

    private BigDecimal totalPrice;
    private BigDecimal reducedPrice;
    private BigDecimal shipmentFee;

    private BigDecimal totalTotalPrice;


}
