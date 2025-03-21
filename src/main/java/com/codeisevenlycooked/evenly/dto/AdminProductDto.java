package com.codeisevenlycooked.evenly.dto;


import com.codeisevenlycooked.evenly.entity.Category;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminProductDto {
    private Long id;

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int price;

    private String description;

    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;

    private Long categoryId;

    private Category category;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private int stock;

    private String status; // "AVAILABLE", "SOLD_OUT", "DELETED"

}
