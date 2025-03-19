package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.ProductResponseDto;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.ProductStatus;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void getAllProducts() {
        //given
        List<Product> products = List.of(
                Product.builder()
                        .id(1L)
                        .name("Apollo Portable Lamp-White opal glass")
                        .price(150000)
                        .imageUrl("https://www.hay.com/img_20240404083132/globalassets/inriver/integration/service/ae378-b508_apollo-portable-white_gb_1220x1220_brandvariant.jpg?w=600")
                        .category("Lighting")
                        .stock(10)
                        .status(ProductStatus.AVAILABLE)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("Barro Plate-Set of 2-Ø18-Dark blue")
                        .price(35000)
                        .imageUrl("https://www.hay.com/img_20230907111544/globalassets/inriver/integration/service/ac459-a668-ai60-02au_barro-plate-oe18-set-of-2-dark-blue_gb_1220x1220_brandvariant.jpg?w=600")
                        .category("Kitchen")
                        .stock(15)
                        .status(ProductStatus.SOLD_OUT)
                        .build()
        );
        given(productRepository.findByStatusNot(ProductStatus.DELETED)).willReturn(products);
        //when
        List<ProductResponseDto> result = productService.getAllProducts();
        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Apollo Portable Lamp-White opal glass");
        assertThat(result.get(1).getStatus()).isEqualTo(ProductStatus.SOLD_OUT);

    }
}