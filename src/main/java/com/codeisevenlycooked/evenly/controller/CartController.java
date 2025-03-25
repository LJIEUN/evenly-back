package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.CartItemDto;
import com.codeisevenlycooked.evenly.dto.CartResponseDto;
import com.codeisevenlycooked.evenly.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(
            @RequestHeader(value = "Authorization", required = false) String token) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        CartResponseDto responseDto = cartService.getCart(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<String> addCart(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CartItemDto cartItemDto) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        cartService.addCart(userId, cartItemDto);
        return ResponseEntity.ok("장바구니에 상품이 추가 되었습니다!");
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<String> UpdateQuantityCart(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long itemId,
            @RequestBody CartItemDto cartItemDto) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        cartService.UpdateQuantityCart(userId, itemId, cartItemDto.getQuantity());
        return ResponseEntity.ok("수량이 수정 되었습니다.");
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> DeleteCartItem(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long itemId) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        cartService.DeleteCartItem(userId, itemId);
        return ResponseEntity.ok("상품이 삭제 되었습니다.");
    }

}
