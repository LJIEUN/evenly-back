package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.CartItemResponseDto;
import com.codeisevenlycooked.evenly.dto.CartResponseDto;
import com.codeisevenlycooked.evenly.entity.Cart;
import com.codeisevenlycooked.evenly.entity.CartItem;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.repository.CartItemRepository;
import com.codeisevenlycooked.evenly.repository.CartRepository;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import com.codeisevenlycooked.evenly.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal(expression = "user") User user) {
        CartResponseDto responseDto = cartService.getCart(user);
        return ResponseEntity.ok(responseDto);
    }

}
