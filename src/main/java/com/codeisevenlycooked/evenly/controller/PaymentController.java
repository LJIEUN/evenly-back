package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.PaymentRequestDto;
import com.codeisevenlycooked.evenly.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequestDto paymentRequestDto) {

        boolean isPaymentSuccess = paymentService.processPayment(paymentRequestDto.getOrderId());

        if (isPaymentSuccess) {
            return ResponseEntity.ok("결제 성공");
        } else {
            return ResponseEntity.status(400).body("결제 실패");
        }
    }
}
