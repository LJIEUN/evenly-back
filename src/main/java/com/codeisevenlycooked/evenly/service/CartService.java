package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.CartItemResponseDto;
import com.codeisevenlycooked.evenly.dto.CartResponseDto;
import com.codeisevenlycooked.evenly.entity.Cart;
import com.codeisevenlycooked.evenly.entity.CartItem;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.repository.CartItemRepository;
import com.codeisevenlycooked.evenly.repository.CartRepository;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartResponseDto getCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 비었습니다."));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        List<CartItemResponseDto> items = cartItems.stream()
                .map(cartItem -> new CartItemResponseDto(
                        cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getImageUrl(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getQuantity(),
                        cartItem.totalPrice()
                )).collect(Collectors.toList());

        int total = items.stream().mapToInt(CartItemResponseDto::getTotalPrice).sum();

        return new CartResponseDto(items,total);
    }

}
