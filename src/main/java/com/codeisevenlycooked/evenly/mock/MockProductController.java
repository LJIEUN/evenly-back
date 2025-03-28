package com.codeisevenlycooked.evenly.mock;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mock/products")
public class MockProductController {

    @GetMapping
    public ResponseEntity<List<MockProductResponse>> getProducts() {
        List<MockProductResponse> products = List.of(
                new MockProductResponse(1L, "Apollo Portable Lamp-White opal glass", 150000, "\n" +
                        "Size:\n" +
                        "H21.8 x W12.5 x L12.5\n" +
                        "Color:\n" +
                        "White\n" +
                        "Material:\n" +
                        "Glass\n" +
                        "Cord:\n" +
                        "100 cm\n" +
                        "Bulb:\n" +
                        "LED G4\n" +
                        "Dimmable:\n" +
                        "Yes\n" +
                        "Switch:\n" +
                        "Touch step dimmer\n" +
                        "IP:\n" +
                        "44\n" +
                        "Supply:\n" +
                        "5V, 2A",
                        "https://www.hay.com/img_20240404083132/globalassets/inriver/integration/service/ae378-b508_apollo-portable-white_gb_1220x1220_brandvariant.jpg?w=600",
                        "Lighting", 10, "AVAILABLE"),
                new MockProductResponse(2L, "Barro Plate-Set of 2-Ø18-Dark blue", 35000, "Color:\n" +
                        "Dark blue" +
                        "Size:\n" +
                        "D18\n",
                        "https://www.hay.com/img_20230907111544/globalassets/inriver/integration/service/ac459-a668-ai60-02au_barro-plate-oe18-set-of-2-dark-blue_gb_1220x1220_brandvariant.jpg?w=600",
                        "Kitchen", 15, "AVAILABLE")
        );
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MockProductResponse> getProductDetail(@PathVariable Long id) {
        MockProductResponse product = new MockProductResponse(id, "Barro Plate-Set of 2-Ø18-Dark blue", 35000, "Color:\n" +
                "Dark blue" +
                "Size:\n" +
                "D18\n",
                "https://www.hay.com/img_20230907111544/globalassets/inriver/integration/service/ac459-a668-ai60-02au_barro-plate-oe18-set-of-2-dark-blue_gb_1220x1220_brandvariant.jpg?w=600",
                "Kitchen", 15, "AVAILABLE");
        return ResponseEntity.ok(product);
    }
}

@Data
@AllArgsConstructor
class MockProductResponse {
    private Long id;
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private String category;
    private int stock;
    private String status;
}
