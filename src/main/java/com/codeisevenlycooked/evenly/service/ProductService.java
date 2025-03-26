package com.codeisevenlycooked.evenly.service;


import com.codeisevenlycooked.evenly.dto.AdminProductDto;
import com.codeisevenlycooked.evenly.dto.ProductResponseDto;
import com.codeisevenlycooked.evenly.entity.Category;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import com.codeisevenlycooked.evenly.repository.CategoryRepository;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //상품 목록 조회 부분 - 페이지네이션, 카테고리 추가
    public Page<ProductResponseDto> getProductsByCategory(Long categoryId, int page) {
        Pageable pageable = PageRequest.of(page - 1, 12, Sort.by("id").descending());

        Page<Product> products;
        if (categoryId != null && categoryId == 1L) {
            Page<Product> original = productRepository.findByStatusNot(ProductStatus.DELETED, pageable);
            List<Product> filtered = original.stream()
                    .filter(p -> p.getCreatedAt().isAfter(LocalDateTime.now().minusDays(30)))
                    .toList();
            products = new PageImpl<>(filtered, pageable, filtered.size());
        } else if (categoryId != null) {
            products = productRepository.findByCategoryIdAndStatusNot(categoryId, ProductStatus.DELETED, pageable);
        } else {
            products = productRepository.findByStatusNot(ProductStatus.DELETED, pageable);
        }

        return products.map(ProductResponseDto::new);
    }

    //상품 상세 조회 부분 ( = 단일 상품 조회)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        return new ProductResponseDto(product);
    }

    /**
     * admin
     */

    @Transactional
    public List<Product> getAllProductsForAdmin() {
        return productRepository.findAll();
    }

    // 등록
    @Transactional
    public void saveProduct(AdminProductDto productDto) {
        Long categoryId = productDto.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        ProductStatus status = productDto.getStock() == 0 ? ProductStatus.SOLD_OUT : ProductStatus.valueOf(productDto.getStatus());

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .imageUrl(productDto.getImageUrl())
                .category(category)
                .stock(productDto.getStock())
                .status(status) // ENUM 변환
                .build();

        productRepository.save(product);
    }

    public Product getProductByIdForAdmin(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    public void updateProduct(Long id, AdminProductDto productDto) {
        Product existingProduct = getProductByIdForAdmin(id);

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        ProductStatus status = ProductStatus.valueOf(productDto.getStatus());
        if (existingProduct.getStatus() == ProductStatus.DELETED) {
            status = existingProduct.getStatus();
        } else if (productDto.getStock() == 0) {
            status = ProductStatus.SOLD_OUT;
        }

        Product updatedProduct = Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .imageUrl(productDto.getImageUrl())
                .category(category)
                .stock(productDto.getStock())
                .status(status)
                .createdAt(existingProduct.getCreatedAt())
                .build();
        productRepository.save(updatedProduct);
    }

    // 수정 폼에서 dto 변환
    public AdminProductDto convertToDto(Product product) {
        return AdminProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .stock(product.getStock())
                .status(product.getStatus().name())
                .build();
    }

    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = getProductByIdForAdmin(id);
        product.changeStatus(ProductStatus.DELETED);

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
