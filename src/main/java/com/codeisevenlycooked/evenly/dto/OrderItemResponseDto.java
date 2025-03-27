package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private int price;
    private int quantity;
    private BigDecimal totalPrice;

}
