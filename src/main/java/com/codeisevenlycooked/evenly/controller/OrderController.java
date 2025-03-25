package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.OrderRequestDto;
import com.codeisevenlycooked.evenly.dto.OrderResponseDto;
import com.codeisevenlycooked.evenly.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Valid @RequestBody OrderRequestDto requestDto) {

        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        OrderResponseDto responseDto = orderService.createOrder(userId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(
            @RequestHeader(value = "Authorization", required = false) String token) {

        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        List<OrderResponseDto> orders = orderService.getOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrderByOrderNumber(
            @RequestHeader(value = "Authorization", required = false) String token, @PathVariable String orderNumber) {

        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        OrderResponseDto order = orderService.getOrderByOrderNumber(userId, orderNumber);
        return ResponseEntity.ok(order);
    }
}
