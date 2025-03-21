package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotEmpty(message = "주문 상품 목록은 비어있을 수 없습니다.")
    private List<OrderItemDto> orderItems;

    @NotBlank(message = "수령인 이름은 필수 입력값입니다.")
    private String receiverName;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

    @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
    private String mobile;

    private String deliveryMessage;

    @NotBlank(message = "결제 방법은 필수 입력값입니다.")
    private PaymentMethod paymentMethod;
}
