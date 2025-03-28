package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.ItemDto;
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
import org.springframework.stereotype.Service;

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

    public CartResponseDto getCart(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 비었습니다."));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        List<CartItemResponseDto> items = cartItems.stream()
                .map(cartItem -> new CartItemResponseDto(
                        cartItem.getId(),
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
    public void addCart(String userId, ItemDto itemDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 없습니다."));

        if (product.getStock() < itemDto.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다. 남은 재고: " + product.getStock());
        }

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        /* 이미 수량이 있을때 수량 증가하는 부분 */
        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + itemDto.getQuantity();

            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException("재고가 부족합니다. 남은 재고: " + product.getStock());
            }

            cartItem.setQuantity(cartItem.getQuantity() + itemDto.getQuantity());
            cartItemRepository.save(cartItem);
        }
        /* 수량이 존재 하지 않을 때 새로 추가하는 부분 */
        else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
        }
    }

    /* 장바구니 상품 수량 수정 */
    public void UpdateQuantityCart(String userId, Long itemId, int quantity) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품 목록이 없습니다."));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("로그인 후 이용해 주세요.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }


        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    /* 장바구니 상품 삭제 */
    public void DeleteCartItem(String userId, Long itemId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품 목록이 없습니다."));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("로그인 후 이용해주세요.");
        }

        cartItemRepository.delete(cartItem);
    }

}
