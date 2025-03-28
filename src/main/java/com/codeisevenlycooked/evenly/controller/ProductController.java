package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.PagedProductResponse;
import com.codeisevenlycooked.evenly.dto.ProductResponseDto;
import com.codeisevenlycooked.evenly.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //상품 목록 조회
    @GetMapping
    public ResponseEntity<PagedProductResponse> getProducts(
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Page<ProductResponseDto> productPage = productService.getProductsByCategory(categoryId, page);
        PagedProductResponse response = new PagedProductResponse(
                productPage.getContent(),
                productPage.getNumber() + 1,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.isFirst(),
                productPage.isLast()
        );
        return ResponseEntity.ok(response);
    }

    //상품 상세 조회 ( = 단일 상품 조회 )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}


