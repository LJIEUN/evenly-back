package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.CartItemDto;
import com.codeisevenlycooked.evenly.dto.CartItemResponseDto;
import com.codeisevenlycooked.evenly.dto.CartResponseDto;
import com.codeisevenlycooked.evenly.entity.Cart;
import com.codeisevenlycooked.evenly.entity.CartItem;
import com.codeisevenlycooked.evenly.entity.Product;
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

import java.math.BigDecimal;
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

        BigDecimal total = items.stream().map(CartItemResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDto(items,total);
    }

    /* 장바구니 상품 추가 */
    public void addCart(User user, CartItemDto cartItemDto) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 없습니다."));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        /* 이미 수량이 있을때 수량 증가하는 부분 */
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
        }
        /* 수량이 존재 하지 않을 때 새로 추가하는 부분 */
        else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemDto.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
        }
    }

}
