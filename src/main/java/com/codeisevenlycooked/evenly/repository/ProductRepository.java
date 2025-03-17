package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
