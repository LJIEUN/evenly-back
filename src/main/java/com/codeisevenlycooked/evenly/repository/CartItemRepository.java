package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.Cart;
import com.codeisevenlycooked.evenly.entity.CartItem;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
}
