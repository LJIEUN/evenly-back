package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.Category;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductResponseDto {
    private final Long id;
    private final String name;
    private final int price;
    private final String description;
    private final String imageUrl;
    private final Category category;
    private final ProductStatus status;
    private final int stock;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
        this.status = product.getStatus();
        this.stock = product.getStock();
    }
}
