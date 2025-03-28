package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.ItemDto;
import com.codeisevenlycooked.evenly.dto.OrderCreationResponseDto;
import com.codeisevenlycooked.evenly.dto.OrderRequestDto;
import com.codeisevenlycooked.evenly.dto.OrderResponseDto;
import com.codeisevenlycooked.evenly.entity.OrderStatus;
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

    @PostMapping("/create")
    public ResponseEntity<OrderCreationResponseDto> createOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid List<ItemDto> itemDto) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        OrderCreationResponseDto responseDto = orderService.createOrder(userId, itemDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderResponseDto> processPayment(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {

        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        OrderResponseDto responseDto = orderService.processPayment(userId, orderRequestDto);

        boolean isPaymentSuccessful = responseDto.getStatus() != OrderStatus.NOT_PAID && responseDto.getOrderNumber() != null;

        if (isPaymentSuccessful) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrderList(
            @RequestHeader("Authorization") String token) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        List<OrderResponseDto> orders = orderService.getOrderListAlreadyPaid(userId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrderDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable String orderNumber) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        OrderResponseDto order = orderService.getOrderDetail(userId, orderNumber);

        return ResponseEntity.ok(order);
    }
}
