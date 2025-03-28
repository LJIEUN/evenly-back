package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "주문 Id는 필수 입력 값입니다.")
    private Long orderId;
    @NotBlank(message = "받는 사람 이름은 필수 입력 값입니다.")
    private String receiverName;
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String mobile;
    private String deliveryMessage;
    @NotNull(message = "결제 방식은 필수 입력 값입니다.")
    private PaymentMethod paymentMethod;

}
