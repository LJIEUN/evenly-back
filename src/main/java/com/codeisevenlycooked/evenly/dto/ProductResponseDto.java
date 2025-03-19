package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final String category;
    private final ProductStatus status;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
        this.status = product.getStatus();
    }
}
