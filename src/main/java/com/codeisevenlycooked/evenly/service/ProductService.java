package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.AdminProductDto;
import com.codeisevenlycooked.evenly.dto.ProductResponseDto;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    /**
     * admin
     */

    public List<Product> getAllProductsForAdmin() {
        return productRepository.findAll();
    }

    // 등록
    public void saveProduct(AdminProductDto productDto) {
        Product product = Product.builder().name(productDto.getName()).price(productDto.getPrice()).description(productDto.getDescription()).imageUrl(productDto.getImageUrl()).category(productDto.getCategory()).stock(productDto.getStock()).status(ProductStatus.valueOf(productDto.getStatus())) // ENUM 변환
                .build();
        productRepository.save(product);
    }

    public Product getProductByIdForAdmin(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    public void updateProduct(Long id, AdminProductDto product) {
        Product existingProduct = getProductByIdForAdmin(id);

        ProductStatus status = ProductStatus.valueOf(product.getStatus());
        if (product.getStock() == 0) {
            status = ProductStatus.SOLD_OUT;
        }

        Product updatedProduct = Product.builder().id(product.getId()).name(product.getName()).price(product.getPrice()).description(product.getDescription()).imageUrl(product.getImageUrl()).category(product.getCategory()).stock(product.getStock()).status(status).createdAt(existingProduct.getCreatedAt()).build();
        productRepository.save(updatedProduct);
    }

    // 수정 폼에서 dto 변환
    public AdminProductDto convertToDto(Product product) {
        return AdminProductDto.builder().id(product.getId()).name(product.getName()).price(product.getPrice()).description(product.getDescription()).imageUrl(product.getImageUrl()).category(product.getCategory()).stock(product.getStock()).status(product.getStatus().name()).build();
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
