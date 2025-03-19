package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.ProductResponseDto;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //상품 목록 조회 부분
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findByStatusNot(ProductStatus.DELETED).stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    //상품 상세 조회 부분 ( = 단일 상품 조회)
    public ProductResponseDto getProductById(Long id) {
         Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
         return new ProductResponseDto(product);
    }
}
