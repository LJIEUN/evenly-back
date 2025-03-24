package com.codeisevenlycooked.evenly.config;

import com.codeisevenlycooked.evenly.entity.Category;
import com.codeisevenlycooked.evenly.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements ApplicationRunner {
    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Category> defaultCategories = List.of(
                new Category(1L, "NEW"),
                new Category(2L, "LIGHTING"),
                new Category(3L, "OFFICE"),
                new Category(4L, "KITCHEN"),
                new Category(5L, "HOME_DECOR")
        );

        for (Category category : defaultCategories) {
            if (!categoryRepository.existsById(category.getId())) {
                categoryRepository.save(category);
            }
        }
    }
}
