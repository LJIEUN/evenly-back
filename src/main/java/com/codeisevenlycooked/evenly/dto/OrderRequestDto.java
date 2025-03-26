package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private Long orderId;

    private String receiverName;
    private String address;
    private String mobile;
    private String deliveryMessage;
    private PaymentMethod paymentMethod;

}
