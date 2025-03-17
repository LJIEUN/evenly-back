package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //상품 목록 조회 부분
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //상품 상세 조회 부분 ( = 단일 상품 조회)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
