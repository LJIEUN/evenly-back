package com.codeisevenlycooked.evenly.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;

    @Min(value = 1, message = "최소 주문 수량은 1개 이상이어야 합니다.")
    private int quantity;
}
