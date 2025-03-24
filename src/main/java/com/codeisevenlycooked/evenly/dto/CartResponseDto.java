package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private List<CartItemResponseDto> cartItems;
    private BigDecimal totalPrice;
}
