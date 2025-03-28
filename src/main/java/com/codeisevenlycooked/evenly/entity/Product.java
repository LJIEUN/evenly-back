package com.codeisevenlycooked.evenly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 5000)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false, columnDefinition = "TIMESTAMP NULL")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }

    public void changeStock(int quantity) {
        if (this.stock - quantity < 0) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stock -= quantity;

        if (this.stock == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }
}
