package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.AdminProductDto;
import com.codeisevenlycooked.evenly.entity.Category;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.repository.CategoryRepository;
import com.codeisevenlycooked.evenly.service.ProductService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public AdminProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    // 목록 조회(status DELETED도 조회 가능)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProductsForAdmin());
        return "admin/products";
    }

    // 상품 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryList", categoryRepository.findAll());
        model.addAttribute("product", new AdminProductDto());
        return "admin/product-form";
    }

    // 상품 등록 처리
    @PostMapping("/new")
    public String create(@ModelAttribute("product") @Validated AdminProductDto productDto,
                         BindingResult result) {
        System.out.println("categoryId: " + productDto.getCategoryId());
        if (result.hasErrors()) {
            return "admin/product-form";
        }

        productService.saveProduct(productDto);
        return "redirect:/admin/products";
    }

    // 상품 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductByIdForAdmin(id);
        AdminProductDto productDto = productService.convertToDto(product);
        model.addAttribute("categoryList", categoryRepository.findAll());
        model.addAttribute("product", productDto);
        return "admin/product-form";
    }

    // 상품 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute("product") @Validated AdminProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/product-form";
        }

        productService.updateProduct(id, productDto);
        return "redirect:/admin/products";
    }

    // 상품 삭제 처리
    @PostMapping("/{id}/delete")
    public String softDelete(@PathVariable Long id) {
        productService.softDeleteProduct(id);
        return "redirect:/admin/products";
    }

    //  상품 영구 삭제 (DB에서 제거)
    @PostMapping("/{id}/permanent-delete")
    public String permanentDelete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
