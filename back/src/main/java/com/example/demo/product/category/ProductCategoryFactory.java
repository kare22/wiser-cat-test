package com.example.demo.product.category;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryFactory {
    final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryFactory(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public void create() {
        Faker faker = new Faker();
        String categoryName;
        int maxAttempts = 10;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            categoryName = generateName(faker);

            ProductCategory existingCategory = productCategoryRepository.findByName(categoryName);

            if (existingCategory == null) {
                ProductCategory category = new ProductCategory();
                category.setName(categoryName);
                productCategoryRepository.save(category);
                return;
            }
        }

        System.out.println("A unique category name could not be generated");
    }

    private String generateName(Faker faker) {
        return faker.commerce().material();
    }
}