package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
    private Long productId;
    private String productName;
    private String imageUrl;
    private int price;
    private int quantity;
    private int totalPrice;
}
