package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.ItemDto;
import com.codeisevenlycooked.evenly.dto.CartResponseDto;
import com.codeisevenlycooked.evenly.global.MessageResponse;
import com.codeisevenlycooked.evenly.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MessageResponse> addCart(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Valid ItemDto itemDto) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        cartService.addCart(userId, itemDto);
        return ResponseEntity.ok(new MessageResponse("장바구니에 상품이 추가 되었습니다!"));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<MessageResponse> UpdateQuantityCart(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long itemId,
            @RequestBody @Valid ItemDto itemDto) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        cartService.UpdateQuantityCart(userId, itemId, itemDto.getQuantity());
        return ResponseEntity.ok(new MessageResponse("수량이 수정 되었습니다."));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<MessageResponse> DeleteCartItem(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long itemId) {
        String accessToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        cartService.DeleteCartItem(userId, itemId);
        return ResponseEntity.ok(new MessageResponse("상품이 삭제 되었습니다."));
    }
}
