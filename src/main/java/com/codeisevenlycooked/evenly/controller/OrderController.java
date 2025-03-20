package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.OrderRequestDto;
import com.codeisevenlycooked.evenly.dto.OrderResponseDto;
import com.codeisevenlycooked.evenly.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Valid @RequestBody OrderRequestDto requestDto) {

        String userId = "test_evenie";

        OrderResponseDto responseDto = orderService.createOrder(userId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
