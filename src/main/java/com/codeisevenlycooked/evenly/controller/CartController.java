package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.CartItemDto;
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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<String> addCart(@RequestBody CartItemDto cartItemDto, @AuthenticationPrincipal(expression = "user") User user) {
        cartService.addCart(user, cartItemDto);
        return ResponseEntity.ok("장바구니에 상품이 추가 되었습니다!");
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<String> UpdateQuantityCart(@AuthenticationPrincipal(expression = "user") User user, @PathVariable Long itemId, @RequestBody CartItemDto cartItemDto) {
        cartService.UpdateQuantityCart(user, itemId, cartItemDto.getQuantity());
        return ResponseEntity.ok("수량이 수정 되었습니다.");
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> DeleteCartItem(@AuthenticationPrincipal(expression = "user") User user, @PathVariable Long itemId) {
        cartService.DeleteCartItem(user, itemId);
        return ResponseEntity.ok("상품이 삭제 되었습니다.");
    }

}
