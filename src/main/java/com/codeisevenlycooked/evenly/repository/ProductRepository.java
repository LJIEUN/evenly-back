package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusNot(ProductStatus status);

    // 페이지네이션
    Page<Product> findByStatusNot(ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryIdAndStatusNot(Long categoryId, ProductStatus status, Pageable pageable);

}
